package stargatetech2.enemy.tileentity;

import net.minecraft.nbt.NBTTagCompound;
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
import stargatetech2.automation.bus.machines.ShieldControllerBusDriver;
import stargatetech2.core.machine.FaceColor;
import stargatetech2.core.machine.TileOwnedMachine;
import stargatetech2.core.machine.tabs.TabAbstractBus.ISyncBusDevice;
import stargatetech2.enemy.util.IonizedParticles;

public class TileShieldController extends TileOwnedMachine implements ISyncBusDevice, IFluidHandler, IShieldController{
	private ShieldControllerBusDriver driver = new ShieldControllerBusDriver();
	private IBusInterface[] interfaces = new IBusInterface[]{
			StargateTechAPI.api().getFactory().getIBusInterface(this, driver)
	};
	
	public FluidTank tank = new FluidTank(16000);
	private ShieldPermissions permissions = ShieldPermissions.getDefault();
	private boolean active = false; // Implement all the things.
	
	@Override
	public void updateEntity(){
		// TODO: implement things.
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
		driver.setEnabled(nbt.getBoolean("enabled"));
	}

	@Override
	protected void writeNBT(NBTTagCompound nbt) {
		nbt.setCompoundTag("tank", tank.writeToNBT(new NBTTagCompound()));
		nbt.setCompoundTag("permissions", permissions.writeToNBT());
		nbt.setShort("address", driver.getInterfaceAddress());
		nbt.setBoolean("enabled", driver.isInterfaceEnabled());
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
}