package stargatetech2.enemy.tileentity;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import stargatetech2.core.machine.FaceColor;
import stargatetech2.core.machine.Inventory;
import stargatetech2.core.machine.TileEntityMachine;

public class TileParticleIonizer extends TileEntityMachine implements IFluidHandler, IEnergyHandler, ISidedInventory{
	public final FluidTank ionizedParticles = new FluidTank(8000);		// orange
	public final FluidTank fluidIonizable = new FluidTank(8000);		// blue
	public final Inventory solidIonizable = new Inventory(9);			// blue
	public final EnergyStorage energy = new EnergyStorage(32000, 400);
	
	@Override
	public void updateEntity(){
		
	}
	
	@Override
	protected void readNBT(NBTTagCompound nbt){
		ionizedParticles.readFromNBT(nbt.getCompoundTag("ionizedParticles"));
		fluidIonizable.readFromNBT(nbt.getCompoundTag("fluidIonizable"));
		solidIonizable.readFromNBT(nbt.getCompoundTag("solidIonizable"));
		readFacingNBT(nbt.getCompoundTag("facing"));
	}
	
	@Override
	protected void writeNBT(NBTTagCompound nbt){
		nbt.setCompoundTag("ionizedParticles", ionizedParticles.writeToNBT(new NBTTagCompound()));
		nbt.setCompoundTag("fluidIonizable", fluidIonizable.writeToNBT(new NBTTagCompound()));
		nbt.setCompoundTag("solidIonizable", solidIonizable.writeToNBT(new NBTTagCompound()));
		nbt.setCompoundTag("facing", writeFacingNBT());
	}
	
	@Override
	protected FaceColor[] getPossibleFaceColors() {
		return new FaceColor[]{FaceColor.VOID, FaceColor.BLUE, FaceColor.ORANGE};
	}
	
	// ############################################################################################
	// IFluidHandler
	@Override
	public int fill(ForgeDirection side, FluidStack resource, boolean doFill) {
		if(getColor(side) == FaceColor.BLUE){
			return fluidIonizable.fill(resource, doFill);
		}
		return 0;
	}

	@Override
	public FluidStack drain(ForgeDirection side, FluidStack resource, boolean doDrain) {
		FluidStack drain = drain(side, resource.amount, false);
		if(drain == null || !resource.isFluidEqual(drain)) return null;
		else return drain(side, resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection side, int maxDrain, boolean doDrain) {
		if(getColor(side) == FaceColor.ORANGE){
			return ionizedParticles.drain(maxDrain, doDrain);
		}
		return null;
	}

	@Override
	public boolean canFill(ForgeDirection side, Fluid fluid) {
		return getColor(side) == FaceColor.BLUE;
	}

	@Override
	public boolean canDrain(ForgeDirection side, Fluid fluid) {
		return getColor(side) == FaceColor.ORANGE;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection side) {
		if(getColor(side) == FaceColor.BLUE){
			return new FluidTankInfo[]{fluidIonizable.getInfo()};
		}else if(getColor(side) == FaceColor.ORANGE){
			return new FluidTankInfo[]{ionizedParticles.getInfo()};
		}else return null;
	}
	
	// ############################################################################################
	// IEnergyHandler
	@Override
	public int receiveEnergy(ForgeDirection side, int maxReceive, boolean simulate) {
		return energy.receiveEnergy(maxReceive, simulate);
	}

	@Override
	public int extractEnergy(ForgeDirection side, int maxExtract, boolean simulate) {
		return 0;
	}

	@Override
	public boolean canInterface(ForgeDirection side) {
		return true;
	}

	@Override
	public int getEnergyStored(ForgeDirection side) {
		return energy.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection side) {
		return energy.getMaxEnergyStored();
	}
	
	// ############################################################################################
	// ISidedInventory
	@Override
	public int getSizeInventory() {
		return solidIonizable.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return solidIonizable.getStackInSlot(slot);
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		return solidIonizable.decrStackSize(slot, amount);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		solidIonizable.setInventorySlotContents(slot, stack);
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return solidIonizable.isItemValidForSlot(slot, stack);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		if(getColor(side) == FaceColor.BLUE){
			return new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
		}
		return new int[]{};
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return getColor(side) == FaceColor.BLUE && solidIonizable.canInsert();
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return getColor(side) == FaceColor.BLUE && solidIonizable.canExtract();
	}
	
	// useless stuff...  :c  (creeper face!)
	@Override public void openChest(){}
	@Override public void closeChest(){}
	@Override public boolean isUseableByPlayer(EntityPlayer entityplayer){ return true; }
	@Override public int getInventoryStackLimit(){ return 64; }
	@Override public String getInvName(){ return "Particle ionizer"; }
	@Override public boolean isInvNameLocalized(){ return true; }
	@Override public ItemStack getStackInSlotOnClosing(int slot){ return null; }
}