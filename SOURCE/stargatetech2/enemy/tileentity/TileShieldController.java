package stargatetech2.enemy.tileentity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import stargatetech2.api.StargateTechAPI;
import stargatetech2.api.bus.IBusInterface;
import stargatetech2.api.shields.IShieldController;
import stargatetech2.api.shields.ShieldPermissions;
import stargatetech2.core.machine.FaceColor;
import stargatetech2.core.machine.TileOwnedMachine;
import stargatetech2.core.machine.tabs.TabAbstractBus.ISyncBusDevice;
import stargatetech2.core.util.Vec3Int;
import stargatetech2.enemy.ModuleEnemy;
import stargatetech2.enemy.bus.ShieldControllerBusDriver;
import stargatetech2.enemy.util.IShieldControllerProvider;
import stargatetech2.enemy.util.IonizedParticles;

public class TileShieldController extends TileOwnedMachine
implements ISyncBusDevice, IFluidHandler, IShieldController, IShieldControllerProvider{
	
	private ShieldControllerBusDriver driver = new ShieldControllerBusDriver();
	private IBusInterface busInterface = StargateTechAPI.api().getFactory().getIBusInterface(this, driver);
	private IBusInterface[] interfaces = new IBusInterface[]{busInterface};
	
	public FluidTank tank = new FluidTank(16000);
	private ShieldPermissions permissions = ShieldPermissions.getDefault();
	private ArrayList<Vec3Int> emitters = new ArrayList();
	private boolean active = false; // Implement all the things.
	
	@Override
	public void updateEntity(){
		// TODO: implement things.
		
		// Attempt to drain particles
		// » if it fails and shield is raised, lower the shield
		// » if it succeeds and shield is down, raise the shield
		
		// Probably snap the check to every 1 or 5 seconds, to keep lag down when supplies are low.
		// Balance it with higher particle consuption per check.
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
		recursiveRemap(worldObj, xCoord, yCoord, zCoord, emitters, memory);
		unreachableDependencies.removeAll(emitters);
		dropAll(unreachableDependencies);
	}
	
	private void dropAll(List<Vec3Int> unreachable){
		for(Vec3Int e : unreachable){
			ModuleEnemy.shieldEmitter.dropSelf(worldObj, e.x, e.y, e.z);
		}
	}
	
	@Override
	public void invalidate(){
		super.invalidate();
		dropAll(emitters);
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
					TileEntity te = w.getBlockTileEntity(nx, ny, nz);
					if(te instanceof TileShieldEmitter){
						found.add(pos);
						recursiveRemap(w, nx, ny, nz, found, memory);
					}
				}
			}
		}
	}
	
	@Override
	protected FaceColor[] getPossibleFaceColors() {
		return new FaceColor[]{FaceColor.VOID, FaceColor.BLUE};
	}
	
	public ShieldPermissions getPermissions(){
		return permissions;
	}
	
	public boolean isShieldOn(){
		return false;
	}
	
	public void updatePermissions(boolean set, int flag){
		if(set){
			permissions.allow(flag);
		}else{
			permissions.disallow(flag);
		}
		updateClients();
	}
	
	public void updateExceptions(boolean set, String name){
		if(set){
			permissions.setPlayerException(name);
		}else{
			permissions.removePlayerException(name);
		}
		updateClients();
	}
	
	public int getIonAmount(){
		return tank.getFluidAmount();
	}
	
	public void setIonAmount(int ions){
		tank.setFluid(new FluidStack(IonizedParticles.fluid, ions));
	}
	
	@Override
	protected void readNBT(NBTTagCompound nbt) {
		tank.readFromNBT(nbt.getCompoundTag("tank"));
		permissions = ShieldPermissions.readFromNBT(nbt.getCompoundTag("permissions"));
		driver.setAddress(nbt.getShort("address"));
		driver.setEnabled(nbt.getBoolean("driverEnabled"));
		int size = nbt.getInteger("emitters");
		emitters = new ArrayList(size);
		for(int i = 0; i < size; i++){
			emitters.add(Vec3Int.fromNBT(nbt.getCompoundTag("emitter_" + i)));
		}
	}

	@Override
	protected void writeNBT(NBTTagCompound nbt) {
		nbt.setCompoundTag("tank", tank.writeToNBT(new NBTTagCompound()));
		nbt.setCompoundTag("permissions", permissions.writeToNBT());
		nbt.setShort("address", driver.getInterfaceAddress());
		nbt.setBoolean("driverEnabled", driver.isInterfaceEnabled());
		nbt.setInteger("emitters", emitters.size());
		for(int i = 0; i < emitters.size(); i++){
			nbt.setCompoundTag("emitter_" + i, emitters.get(i).toNBT());
		}
	}
	
	
	// ***************************************************
	// ISyncBusDevice
	
	@Override
	public IBusInterface[] getInterfaces(int side) {
		if(getColor(side).isInput()){
			return interfaces;
		}
		return null;
	}

	@Override
	public World getWorld() {
		return worldObj;
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
	public void setEnabled(boolean enabled) {
		driver.setEnabled(enabled);
		updateClients();
	}

	@Override
	public boolean getEnabled() {
		return driver.isInterfaceEnabled();
	}

	@Override
	public void setAddress(short addr) {
		driver.setAddress(addr);
		updateClients();
	}

	@Override
	public short getAddress() {
		return driver.getInterfaceAddress();
	}
	
	
	// ***************************************************
	// IFluidHandler
	
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		if(getColor(from).isInput() && resource.getFluid() == IonizedParticles.fluid){
			return tank.fill(resource, doFill);
		}else{
			return 0;
		}
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
		return getColor(from).isInput();
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		if(getColor(from).isInput()){
			return new FluidTankInfo[]{tank.getInfo()};
		}else{
			return new FluidTankInfo[]{};
		}
	}

	@Override
	public Vec3Int getShieldControllerCoords() {
		return new Vec3Int(xCoord, yCoord, zCoord);
	}
}