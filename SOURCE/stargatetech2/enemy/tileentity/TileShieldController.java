package stargatetech2.enemy.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import stargatetech2.api.bus.IBusDevice;
import stargatetech2.api.bus.IBusInterface;
import stargatetech2.api.shields.ShieldPermissions;
import stargatetech2.core.machine.FaceColor;
import stargatetech2.core.machine.TileEntityMachine;
import stargatetech2.enemy.util.IonizedParticles;

public class TileShieldController extends TileEntityMachine implements IBusDevice, IFluidHandler{
	private IBusInterface[] interfaces = new IBusInterface[]{};
	public FluidTank tank = new FluidTank(16000);
	private String owner = null;
	private ShieldPermissions permissions = ShieldPermissions.getDefault();
	
	@Override
	protected FaceColor[] getPossibleFaceColors() {
		return new FaceColor[]{FaceColor.VOID, FaceColor.BLUE};
	}
	
	public void setOwner(EntityPlayer player){
		if(owner == null)
			owner = player.getEntityName();
	}
	
	public boolean isOwner(EntityPlayer player){
		return owner == null ? true : player.getEntityName().contentEquals(owner);
	}
	
	// required by the shield
	public ShieldPermissions getPermissions(){
		return permissions;
	}
	
	// required by the Naquadah Rails
	public boolean isShieldOn(){
		return false;
	}
	
	// required by packets
	public void updatePermissions(boolean set, int flag){
		if(set){
			permissions.allow(flag);
		}else{
			permissions.disallow(flag);
		}
		updateClients();
	}
	
	// required by packets
	public void updateExceptions(boolean set, String name){
		if(set){
			permissions.setPlayerException(name);
		}else{
			permissions.removePlayerException(name);
		}
		updateClients();
	}
	
	// required by container
	public int getIonAmount(){
		return tank.getFluidAmount();
	}
	
	// required by container
	public void setIonAmount(int ions){
		tank.setFluid(new FluidStack(IonizedParticles.fluid, ions));
	}
	
	@Override
	protected void readNBT(NBTTagCompound nbt) {
		tank.readFromNBT(nbt.getCompoundTag("tank"));
		owner = nbt.getString("owner");
		permissions = ShieldPermissions.readFromNBT(nbt.getCompoundTag("permissions"));
	}

	@Override
	protected void writeNBT(NBTTagCompound nbt) {
		nbt.setCompoundTag("tank", tank.writeToNBT(new NBTTagCompound()));
		nbt.setString("owner", owner);
		nbt.setCompoundTag("permissions", permissions.writeToNBT());
	}
	
	
	// ***************************************************
	// IBusDevice
	
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