package lordfokas.stargatetech2.modules.enemy.tileentity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import lordfokas.stargatetech2.api.StargateTechAPI;
import lordfokas.stargatetech2.api.bus.IBusInterface;
import lordfokas.stargatetech2.api.shields.IShieldable;
import lordfokas.stargatetech2.api.shields.ITileShieldController;
import lordfokas.stargatetech2.api.shields.ShieldPermissions;
import lordfokas.stargatetech2.lib.tileentity.ITile;
import lordfokas.stargatetech2.lib.tileentity.ITileContext;
import lordfokas.stargatetech2.modules.ModuleEnemy;
import lordfokas.stargatetech2.modules.automation.ISyncBusDevice;
import lordfokas.stargatetech2.modules.enemy.BlockShield;
import lordfokas.stargatetech2.modules.enemy.IShieldControllerProvider;
import lordfokas.stargatetech2.modules.enemy.ShieldControllerBusDriver;
import lordfokas.stargatetech2.modules.enemy.TileShieldEmitter;
import lordfokas.stargatetech2.util.Vec3Int;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class ShieldControllerServer extends ShieldControllerCommon
implements ITileContext.Server, IShieldControllerProvider, ITileShieldController, ISyncBusDevice, IFluidHandler{
	private static final int ION_DRAIN = 10;
	
	private ShieldControllerBusDriver driver = new ShieldControllerBusDriver(this);
	private IBusInterface busInterface = StargateTechAPI.api().getFactory().getIBusInterface(this, driver);
	private IBusInterface[] interfaces = new IBusInterface[]{busInterface};
	private ArrayList<Vec3Int> emitters = new ArrayList();
	private LinkedList<Vec3Int> shields = new LinkedList();
	private ITile.Server tile;
	
	@Override
	public void setTile(ITile.Server tile) {
		this.tile = tile;
	}
	
	@Override
	public boolean canTick() {
		return true;
	}
	
	@Override
	public void tick() {
		if((tile.getWorld().getTotalWorldTime() % 100) != 0) return;
		if(active && !enabled) lowerShields();
		if(enabled && hasIons()){
			tank.drain(ION_DRAIN, true);
			if(!active && enabled) raiseShields();
		}else{
			if( active && enabled) lowerShields();
		}
	}
	
	private boolean hasIons(){
		return tank.getFluidAmount() >= ION_DRAIN;
	}
	
	private void raiseShields(){
		shields.clear();
		for(Vec3Int pos : emitters){ // TODO: fix this shit
			TileEntity te = tile.getWorld().getTileEntity(pos.x, pos.y, pos.z);
			if(te instanceof TileShieldEmitter){
				shields.addAll(((TileShieldEmitter)te).createShields());
			}
		}
		active = true;
	}
	
	private void lowerShields(){
		for(Vec3Int pos : shields){ // TODO: fix this shit
			Block b = tile.getWorld().getBlock(pos.x, pos.y, pos.z);
			if(b instanceof BlockShield){
				tile.getWorld().setBlockToAir(pos.x, pos.y, pos.z);
			}else if(b instanceof IShieldable){
				((IShieldable)b).onUnshield(tile.getWorld(), pos.x, pos.y, pos.z);
			}
		}
		shields.clear();
		active = false;
	}
	
	@Override
	public ShieldPermissions getPermissions(){
		return permissions;
	}
	
	@Override
	public boolean isShieldOn(){
		return active;
	}
	
	@Override
	public String getOwner() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public boolean hasAccess(String player) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public Vec3Int getShieldControllerCoords() {
		return new Vec3Int(tile.x(), tile.y(), tile.z());
	}
	
	public void setShieldStatus(boolean enabled){
		this.enabled = enabled;
		if(enabled && !active && hasIons()) raiseShields();
		if(!enabled && active) lowerShields();
	}
	
	public void addEmitter(TileShieldEmitter emitter){
		emitters.add(new Vec3Int(emitter.xCoord, emitter.yCoord, emitter.zCoord));
	}
	
	public void removeEmitter(TileShieldEmitter emitter){
		emitters.remove(new Vec3Int(emitter.xCoord, emitter.yCoord, emitter.zCoord));
		ArrayList<Vec3Int> unreachableDependencies = emitters;
		LinkedList<Vec3Int> memory = new LinkedList();
		memory.add(getShieldControllerCoords());
		emitters = new ArrayList();
		recursiveRemap(tile.getWorld(), tile.x(), tile.y(), tile.z(), emitters, memory);
		unreachableDependencies.removeAll(emitters);
		dropAll(unreachableDependencies);
	}
	
	private void dropAll(List<Vec3Int> unreachable){
		boolean shieldsUp = active;
		if(shieldsUp) lowerShields();
		for(Vec3Int e : unreachable){
			ModuleEnemy.shieldEmitter.dropSelf(tile.getWorld(), e.x, e.y, e.z);
		}
		if(shieldsUp) raiseShields();
	}
	
	private void recursiveRemap(World w, int x, int y, int z, ArrayList<Vec3Int> found, LinkedList<Vec3Int> memory){
		for(ForgeDirection fd : ForgeDirection.VALID_DIRECTIONS){
			int nx = x + fd.offsetX;
			int ny = y + fd.offsetY;
			int nz = z + fd.offsetZ;
			if(ny >= 0 && ny < w.getActualHeight()){
				Vec3Int pos = new Vec3Int(nx, ny, nz);
				if(!memory.contains(pos)){
					memory.add(pos);
					TileEntity te = w.getTileEntity(nx, ny, nz);
					if(te instanceof TileShieldEmitter){
						found.add(pos);
						recursiveRemap(w, nx, ny, nz, found, memory);
					}
				}
			}
		}
	}
	
	public void updatePermissions(boolean set, int flag){
		if(set){
			permissions.allow(flag);
		}else{
			permissions.disallow(flag);
		}
		tile.updateClients();
	}
	
	public void updateExceptions(boolean set, String name){
		if(set){
			permissions.setPlayerException(name);
		}else{
			permissions.removePlayerException(name);
		}
		tile.updateClients();
	}
	
	
	// ***************************************************
	// ISyncBusDevice
	
	@Override public IBusInterface[] getInterfaces(int side){ return interfaces; }
	@Override public boolean getEnabled(){ return driver.isInterfaceEnabled(); }
	@Override public short getAddress(){ return driver.getInterfaceAddress(); }
	
	@Override
	public void setAddress(short addr){
		driver.setAddress(addr);
		super.busAddress = addr;
	}
	
	@Override
	public void setEnabled(boolean enabled){
		driver.setEnabled(enabled);
		super.busEnabled = enabled;
	}
	
	
	// ***************************************************
	// IFluidHandler
	
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		//if(getColor(from).isInput() && resource.getFluid() == IonizedParticles.fluid){
			return tank.fill(resource, doFill);
		//}else{
		//	return 0;
		//}
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return false; //getColor(from).isInput();
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		//if(getColor(from).isInput()){
			return new FluidTankInfo[]{tank.getInfo()};
		//}else{
		//	return new FluidTankInfo[]{};
		//}
	}
	
	
	// ***************************************************
	// Named Binary Tags
	
	@Override
	public void readNBTData(NBTTagCompound nbt) {
		super.readNBTData(nbt);
		driver.setAddress(nbt.getShort("address"));
		driver.setEnabled(nbt.getBoolean("driverEnabled"));
		int num_emitters = nbt.getInteger("emitters");
		emitters = new ArrayList(num_emitters);
		for(int i = 0; i < num_emitters; i++){
			emitters.add(Vec3Int.fromNBT(nbt.getCompoundTag("emitter_" + i)));
		}
		int num_shields = nbt.getInteger("shields");
		shields.clear();
		for(int i = 0; i < num_shields; i++){
			shields.add(Vec3Int.fromNBT(nbt.getCompoundTag("shield_" + i)));
		}
	}
	
	@Override
	public void writeNBTData(NBTTagCompound nbt) {
		super.writeNBTData(nbt);
		nbt.setInteger("emitters", emitters.size());
		for(int i = 0; i < emitters.size(); i++){
			nbt.setTag("emitter_" + i, emitters.get(i).toNBT());
		}
		nbt.setInteger("shields", shields.size());
		for(int i = 0; i < shields.size(); i++){
			nbt.setTag("shield_" + i, shields.get(i).toNBT());
		}
		nbt.setShort("address", driver.getInterfaceAddress());
		nbt.setBoolean("driverEnabled", driver.isInterfaceEnabled());
	}
}