package stargatetech2.core.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import stargatetech2.api.StargateTechAPI;
import stargatetech2.api.bus.BusPacketLIP;
import stargatetech2.api.bus.BusPacketLIP.LIPMetadata;
import stargatetech2.api.bus.IBusDevice;
import stargatetech2.api.bus.IBusInterface;
import stargatetech2.api.stargate.Address;
import stargatetech2.api.stargate.ITileStargateBase;
import stargatetech2.common.base.BaseTileEntity;
import stargatetech2.common.reference.ModReference;
import stargatetech2.core.ModuleCore;
import stargatetech2.core.network.bus.machines.StargateBusDriver;
import stargatetech2.core.network.stargate.StargateNetwork;
import stargatetech2.core.network.stargate.Wormhole;
import stargatetech2.core.packet.PacketWormhole;
import stargatetech2.core.worldgen.lists.StargateBuildList;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileStargate extends BaseTileEntity implements ITileStargateBase, IBusDevice{
	@ServerLogic private boolean isInvalidating = false;
	@ServerLogic private Wormhole wormhole = null;
	@ServerLogic private boolean isSource = false;
	@ServerLogic private boolean useXBuilder;
	
	@ClientLogic private RenderData renderData = new RenderData();
	
	private StargateBusDriver networkDriver = new StargateBusDriver(this);
	private IBusInterface[] interfaces = new IBusInterface[]{
			StargateTechAPI.api().getFactory().getIBusInterface(this, networkDriver)
	};
	
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
			ModuleCore.stargate.dropStargate(worldObj, xCoord, yCoord, zCoord);
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
	public boolean dial(Address address){
		if(worldObj.isRemote || wormhole != null) return false;
		StargateNetwork.instance().dial(getAddress(), address);
		return hasActiveWormhole();
	}
	
	@ServerLogic
	public boolean hasActiveWormhole() {
		return wormhole != null && wormhole.isActive();
	}
	
	@ServerLogic
	public void setWormhole(Wormhole wormhole, boolean isSource, boolean broadcast){
		this.wormhole = wormhole;
		this.isSource = isSource;
		PacketWormhole.sendSync(xCoord, yCoord, zCoord, true).sendToAllInDim(worldObj.provider.dimensionId);
		Address address = isSource ? wormhole.getDestinationAddress() : wormhole.getSourceAddress();
		if(broadcast){
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
	public void onDisconnect(){
		wormhole = null;
		PacketWormhole.sendSync(xCoord, yCoord, zCoord, false).sendToAllInDim(worldObj.provider.dimensionId);
	}
	
	@Override
	public void setWorldObj(World w){
		super.setWorldObj(w);
		if(w.isRemote){
			PacketWormhole.syncRequest(xCoord, yCoord, zCoord).sendToServer();
		}
	}
	
	@ClientLogic
	private void clientTick(){
		
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
		return AxisAlignedBB.getAABBPool().getAABB(xCoord - 2, yCoord, zCoord-2, xCoord + 3, yCoord+5, zCoord+3);
	}
	
	@Override
	protected void readNBT(NBTTagCompound nbt) {
		interfaces[0].readFromNBT(nbt, "interface");
		useXBuilder = nbt.getBoolean("useXBuilder");
		renderData.hasWormhole = nbt.getBoolean("hasWormhole");
	}

	@Override
	protected void writeNBT(NBTTagCompound nbt) {
		interfaces[0].writeToNBT(nbt, "interface");
		nbt.setBoolean("useXBuilder", useXBuilder);
		nbt.setBoolean("hasWormhole", hasActiveWormhole());
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
}