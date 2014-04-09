package stargatetech2.enemy.tileentity;

import java.util.ArrayList;
import java.util.List;

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
import stargatetech2.automation.bus.StandardBusDriver;
import stargatetech2.core.machine.FaceColor;
import stargatetech2.core.machine.TileOwnedMachine;
import stargatetech2.core.machine.tabs.TabAbstractBus.ISyncBusDevice;
import stargatetech2.core.util.Vec3Int;
import stargatetech2.enemy.bus.ShieldControllerBusDriver;
import stargatetech2.enemy.bus.ShieldControllerBusPacket;
import stargatetech2.enemy.util.IonizedParticles;

public class TileShieldController extends TileOwnedMachine implements ISyncBusDevice, IFluidHandler, IShieldController{
	private ShieldControllerBusDriver networkDriver = new ShieldControllerBusDriver();
	private StandardBusDriver emitterDriver = new StandardBusDriver((short)0x0000);
	private IBusInterface networkInterface = StargateTechAPI.api().getFactory().getIBusInterface(this, networkDriver);
	private IBusInterface emitterInterface = StargateTechAPI.api().getFactory().getIBusInterface(this, emitterDriver);
	private IBusInterface[] interfaces = new IBusInterface[]{ networkInterface, emitterInterface };
	
	public FluidTank tank = new FluidTank(16000);
	private ShieldPermissions permissions = ShieldPermissions.getDefault();
	private boolean active = false; // Implement all the things.
	
	@Override
	public void updateEntity(){
		// TODO: implement things.
	}
	
	private void updateShields(){
		emitterDriver.addpacketToQueue(new ShieldControllerBusPacket(active, new Vec3Int(xCoord, yCoord, zCoord)));
		emitterInterface.sendAllPackets();
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
		networkDriver.setAddress(nbt.getShort("address"));
		networkDriver.setEnabled(nbt.getBoolean("enabled"));
	}

	@Override
	protected void writeNBT(NBTTagCompound nbt) {
		nbt.setCompoundTag("tank", tank.writeToNBT(new NBTTagCompound()));
		nbt.setCompoundTag("permissions", permissions.writeToNBT());
		nbt.setShort("address", networkDriver.getInterfaceAddress());
		nbt.setBoolean("enabled", networkDriver.isInterfaceEnabled());
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
		networkDriver.setEnabled(enabled);
		updateClients();
	}

	@Override
	public boolean getEnabled() {
		return networkDriver.isInterfaceEnabled();
	}

	@Override
	public void setAddress(short addr) {
		networkDriver.setAddress(addr);
		updateClients();
	}

	@Override
	public short getAddress() {
		return networkDriver.getInterfaceAddress();
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