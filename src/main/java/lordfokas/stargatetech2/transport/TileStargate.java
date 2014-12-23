package lordfokas.stargatetech2.transport;

import lordfokas.stargatetech2.ModuleTransport;
import lordfokas.stargatetech2.api.StargateTechAPI;
import lordfokas.stargatetech2.api.bus.BusPacketLIP;
import lordfokas.stargatetech2.api.bus.BusPacketLIP.LIPMetadata;
import lordfokas.stargatetech2.api.bus.IBusDevice;
import lordfokas.stargatetech2.api.bus.IBusInterface;
import lordfokas.stargatetech2.api.stargate.Address;
import lordfokas.stargatetech2.api.stargate.DialError;
import lordfokas.stargatetech2.api.stargate.ITileStargateBase;
import lordfokas.stargatetech2.api.stargate.StargateEvent;
import lordfokas.stargatetech2.core.base.BaseTileEntity;
import lordfokas.stargatetech2.core.reference.ModReference;
import lordfokas.stargatetech2.transport.stargates.StargateNetwork;
import lordfokas.stargatetech2.transport.stargates.Wormhole;
import lordfokas.stargatetech2.world.genlists.StargateBuildList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileStargate extends BaseTileEntity implements ITileStargateBase, IBusDevice, IEnergyHandler{
	private static final int DIAL_COST_8 = 20000;		// 20k
	private static final int DIAL_COST_9 = 100000000;	// 100M
	
	@ServerLogic private boolean isInvalidating = false;
	@ServerLogic private Wormhole wormhole = null;
	@ServerLogic private boolean isSource = false;
	@ServerLogic private boolean useXBuilder;
	
	@ClientLogic private RenderData renderData = new RenderData();
	
	private StargateBusDriver networkDriver = new StargateBusDriver(this);
	private IBusInterface[] interfaces = new IBusInterface[]{ StargateTechAPI.api().getFactory().getIBusInterface(this, networkDriver) };
	private EnergyStorage capacitor = new EnergyStorage(400000, 10000);
	
	@ClientLogic
	public static class RenderData{
		public static class ChevronData{
			public boolean isLit = false;
			public float position = 0.0F;
			public float dir = 0.0F;
		}
		
		public boolean hasWormhole = false;
		public float dTheta = 0F;
		public float curr_theta = 0F;
		private ChevronData[] chevron;
		
		public RenderData(){
			chevron = new ChevronData[9];
			for(int i = 0; i < 9; i++){
				chevron[i] = new ChevronData();
				chevron[i].isLit = (i < 4 || i > 5);
			}
		}
		
		public ChevronData getChevron(int c){
			return chevron[c];
		}
	}
	
	@Override
	public void validate(){
		super.validate();
		getAddress(); // forces the Stargate Network to generate an address for this Stargate.
	}
	
	@Override
	public void invalidate(){
		isInvalidating = true;
		super.invalidate();
		removeDependencies();
		if(StargateNetwork.instance().isLoaded()){
			StargateNetwork.instance().freeMyAddress(worldObj, xCoord, yCoord, zCoord);
		}
	}
	
	@Override
	public void updateEntity(){
		if(worldObj.isRemote){
			clientTick();
		}else{
			serverTick();
		}
	}
	
	public void setDirectionX(boolean isX){
		useXBuilder = isX;
	}
	
	public boolean isX(){
		return useXBuilder;
	}
	
	@ServerLogic
	private void removeDependencies(){
		if(!worldObj.isRemote){
			StargateBuildList builder;
			if(useXBuilder){
				builder = StargateBuildList.SGX;
			}else{
				builder = StargateBuildList.SGZ;
			}
			builder.delete(worldObj, xCoord, yCoord, zCoord);
		}
	}
	
	@ServerLogic
	private void serverTick(){
		if(hasActiveWormhole() && isSource){
			wormhole.update();
		}
	}
	
	@ServerLogic
	public void destroyStargate(){
		if(!isInvalidating){
			isInvalidating = true;
			MinecraftForge.EVENT_BUS.post(new StargateEvent.StargateDestroyed(getAddress(), worldObj, xCoord, yCoord, zCoord));
			ModuleTransport.stargate.dropStargate(worldObj, xCoord, yCoord, zCoord);
		}
	}
	
	@Override
	public Address getAddress(){
		if(worldObj.isRemote)
			return null;
		else
			return StargateNetwork.instance().getMyAddress(worldObj, xCoord, yCoord, zCoord);
	}
	
	@Override
	@ServerLogic
	public DialError dial(Address address, int timeout, DialMethod method){
		if(worldObj.isRemote) return DialError.UNKNOWN_LOGIC_ERROR;
		if(wormhole != null) return DialError.SOURCE_GATE_BUSY;
		if(timeout < 1 || timeout > 38) timeout = 38;
		return StargateNetwork.instance().dial(getAddress(), address, timeout);
	}
	
	@ServerLogic
	public boolean hasActiveWormhole() {
		return wormhole != null && wormhole.isActive();
	}
	
	@ServerLogic
	public boolean canDial(int addressSize){
		if(hasActiveWormhole()){
			return false;
		}
		switch(addressSize){
			case 7: return true;
			case 8: return capacitor.getEnergyStored() >= DIAL_COST_8;
			case 9: return capacitor.getEnergyStored() >= DIAL_COST_9;
		}
		return false;
	}
	
	@ServerLogic
	public void setWormhole(Wormhole wormhole, boolean isSource, boolean broadcast){
		this.wormhole = wormhole;
		this.isSource = isSource;
		PacketWormhole.sendSync(xCoord, yCoord, zCoord, true).sendToClientsInDim(worldObj.provider.dimensionId);
		Address address = isSource ? wormhole.getDestinationAddress() : wormhole.getSourceAddress();
		if(broadcast){
			if(isSource && address.length() == 8){
				capacitor.extractEnergy(DIAL_COST_8, false);
			}
			BusPacketLIP packet = new BusPacketLIP(networkDriver.getInterfaceAddress(), (short)0xFFFF);
			packet.setMetadata(new LIPMetadata(ModReference.MOD_ID, "Stargate", ""));
			packet.set(".protocol", "Stargate Protocol");
			packet.set(".target", "ANY");
			packet.set("action", "dialing");
			packet.set("direction", isSource ? "outgoing" : "incoming");
			packet.set("address", address.toString());
			packet.finish();
			networkDriver.addPacket(packet);
			interfaces[0].sendAllPackets();
		}
	}
	
	@ServerLogic
	public void disconnect(){
		if(wormhole != null){
			wormhole.disconnect();
		}
	}
	
	@ServerLogic
	public void onDisconnect(){
		wormhole = null;
		PacketWormhole.sendSync(xCoord, yCoord, zCoord, false).sendToClientsInDim(worldObj.provider.dimensionId);
		BusPacketLIP packet = new BusPacketLIP(networkDriver.getInterfaceAddress(), (short)0xFFFF);
		packet.setMetadata(new LIPMetadata(ModReference.MOD_ID, "Stargate", ""));
		packet.set(".protocol", "Stargate Protocol");
		packet.set(".target", "ANY");
		packet.set("action", "disconnecting");
		packet.finish();
		networkDriver.addPacket(packet);
		interfaces[0].sendAllPackets();
	}
	
	// TODO: Implement these, in the future.
	@ServerLogic public void openIris(){}
	@ServerLogic public void closeIris(){}
	
	@Override
	public void setWorldObj(World w){
		super.setWorldObj(w);
		if(w.isRemote){
			PacketWormhole.syncRequest(xCoord, yCoord, zCoord).sendToServer();
		}
	}
	
	@ClientLogic
	private void clientTick(){
		// TODO: implement gate animation.
	}
	
	@ClientLogic
	public RenderData getRenderData(){
		return renderData;
	}
	
	@ClientLogic
	public void setHasWormhole(boolean hasWormhole){
		renderData.hasWormhole = hasWormhole;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public double getMaxRenderDistanceSquared(){
		return 0x10000;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox(){
		return AxisAlignedBB.getBoundingBox(xCoord - 2, yCoord, zCoord-2, xCoord + 3, yCoord+5, zCoord+3);
	}
	
	@Override
	protected void readNBT(NBTTagCompound nbt) {
		interfaces[0].readFromNBT(nbt, "interface");
		useXBuilder = nbt.getBoolean("useXBuilder");
		renderData.hasWormhole = nbt.getBoolean("hasWormhole");
		capacitor.readFromNBT(nbt);
	}

	@Override
	protected void writeNBT(NBTTagCompound nbt) {
		interfaces[0].writeToNBT(nbt, "interface");
		nbt.setBoolean("useXBuilder", useXBuilder);
		nbt.setBoolean("hasWormhole", hasActiveWormhole());
		capacitor.writeToNBT(nbt);
	}

	@Override
	public IBusInterface[] getInterfaces(int side) {
		return side == 1 ? null : interfaces;
	}
	
	@Override
	public int getXCoord() {
		return xCoord;
	}

	@Override
	public int getYCoord() {
		return yCoord;
	}

	@Override
	public int getZCoord() {
		return zCoord;
	}

	@Override
	public World getWorld() {
		return worldObj;
	}
	
	// #########################################################
	// IEnergyHandler

	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		return capacitor.receiveEnergy(maxReceive, simulate);
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		return capacitor.extractEnergy(maxExtract, simulate);
	}

	@Override
	public boolean canConnectEnergy(ForgeDirection from) {
		return from != ForgeDirection.UP;
	}

	@Override
	public int getEnergyStored(ForgeDirection from) {
		return capacitor.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return capacitor.getMaxEnergyStored();
	}
}