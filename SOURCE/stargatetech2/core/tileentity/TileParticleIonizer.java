package stargatetech2.core.tileentity;

import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import stargatetech2.common.base.BaseTileEntity;
import stargatetech2.core.util.IonizedParticles;
import stargatetech2.core.util.ParticleIonizerRecipes;

public class TileParticleIonizer extends BaseTileEntity implements IFluidHandler, IPowerReceptor, IInventory{
	private FluidTank tank = new FluidTank(IonizedParticles.fluid, 120000, 120000);
	private ItemStack[] inventory = new ItemStack[9];
	private PowerHandler powerHandler = new PowerHandler(this, Type.MACHINE);
	
	public TileParticleIonizer(){
		powerHandler.configure(50, 100, 5, 16000);
		powerHandler.configurePowerPerdition(0, 0);
	}
	
	@Override
	public void updateEntity(){
		if(worldObj.isRemote == false && worldObj.getWorldTime() % 20 == 0){
			
			updateClients();
		}
	}
	
	public int getIonAmount(){
		FluidStack fs = tank.getInfo().fluid;
		return (fs == null) ? 0 : fs.amount;
	}
	
	@Override
	protected void readNBT(NBTTagCompound nbt) {
		tank.readFromNBT(nbt);
		powerHandler.readFromNBT(nbt.getCompoundTag("powerHandler"));
	}
	
	@Override
	protected void writeNBT(NBTTagCompound nbt) {
		tank.writeToNBT(nbt);
		NBTTagCompound ph = new NBTTagCompound();
		powerHandler.writeToNBT(ph);
		nbt.setCompoundTag("powerHandler", ph);
	}
	
	//##################################################################################
	// IPowerReceptor
	@Override
	public PowerReceiver getPowerReceiver(ForgeDirection side) {
		return powerHandler.getPowerReceiver();
	}
	
	@Override public void doWork(PowerHandler workProvider) {}
	
	@Override
	public World getWorld() {
		return worldObj;
	}
	
	//##################################################################################
	// IFluidHandler
	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		if(resource.fluidID == IonizedParticles.fluid.getID()){
			FluidStack fs = tank.drain(resource.amount, doDrain);
			if(doDrain) updateClients();
			return fs;
		}else{
			return null;
		}
	}
	
	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		FluidStack fs = tank.drain(maxDrain, doDrain);
		if(doDrain) updateClients();
		return fs;
	}
	
	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return fluid.getID() == IonizedParticles.fluid.getID();
	}
	
	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[]{tank.getInfo()};
	}
	
	@Override public boolean canFill(ForgeDirection from, Fluid fluid){ return false; }
	@Override public int fill(ForgeDirection from, FluidStack resource, boolean doFill){ return 0; }
	
	//##################################################################################
	// IInventory
	@Override
	public ItemStack getStackInSlot(int slot) {
		return inventory[slot];
	}
	
	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		ItemStack removed = inventory[slot].copy();
		inventory[slot].stackSize -= amount;
		removed.stackSize = amount;
		return removed;
	}
	
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		inventory[slot] = stack;
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return ParticleIonizerRecipes.getRecipe(stack) != null;
	}
	
	@Override public ItemStack getStackInSlotOnClosing(int i){ return null; }
	@Override public String getInvName(){ return "Particle Ionizer"; }
	@Override public boolean isInvNameLocalized(){ return true; }
	@Override public int getInventoryStackLimit(){ return 64; }
	@Override public boolean isUseableByPlayer(EntityPlayer entityplayer){ return true; }
	@Override public int getSizeInventory(){ return 9; }
	@Override public void openChest(){}
	@Override public void closeChest(){}
}