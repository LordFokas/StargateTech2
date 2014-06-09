package stargatetech2.factory.tileentity;

import java.util.LinkedList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;
import stargatetech2.core.machine.FaceColor;
import stargatetech2.core.machine.TileMachine;
import stargatetech2.factory.util.Buffer;
import stargatetech2.factory.util.Buffer.BufferType;
import stargatetech2.factory.util.BufferEnergy;
import stargatetech2.factory.util.BufferFluid;
import stargatetech2.factory.util.BufferItem;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyStorage;

public class TilePrioritizer extends TileMachine implements ISidedInventory, IFluidHandler, IEnergyHandler{
	private boolean multipleInputs = true;
	private Buffer main, primary, secondary;
	
	@Override
	public void updateEntity(){
		if(multipleInputs){
			primary.transferTo(main);
			if(main.getFill() < 0.5F){
				secondary.transferTo(main);
			}
		}else{
			main.transferTo(primary);
			if(primary.getFill() == 1F && main.getFill() == 1F){
				main.transferTo(secondary);
			}
		}
	}
	
	@Override
	protected FaceColor[] getPossibleFaceColors() {
		if(multipleInputs){ // multiple inputs, one output.
			return new FaceColor[]{FaceColor.PURPLE, FaceColor.GREEN, FaceColor.ORANGE};
		}else{ // one input, multiple outputs.
			return new FaceColor[]{FaceColor.BLUE, FaceColor.RED, FaceColor.YELLOW};
		}
	}
	
	public void setBufferType(int type){
		setBufferType(BufferType.values()[type]);
	}
	
	public void setBufferType(BufferType type){
		switch(type){
		case ENERGY:
			main = new BufferEnergy(60000);
			primary = new BufferEnergy(60000);
			secondary = new BufferEnergy(60000);
			break;
		case FLUID:
			main = new BufferFluid(15000);
			primary = new BufferFluid(15000);
			secondary = new BufferFluid(15000);
			break;
		case ITEM:
			main = new BufferItem(8);
			primary = new BufferItem(8);
			secondary = new BufferItem(8);
			break;
		default: throw new RuntimeException("Illegal buffer type: " + type);
		}
	}
	
	@Override
	protected void readNBT(NBTTagCompound nbt) {
		multipleInputs = nbt.getBoolean("multipleInputs");
		setBufferType(nbt.getInteger("bufferType"));
		main.readFromNBT(nbt.getCompoundTag("main"));
		primary.readFromNBT(nbt.getCompoundTag("primary"));
		secondary.readFromNBT(nbt.getCompoundTag("secondary"));
		readFacingNBT(nbt.getCompoundTag("faces"));
	}
	
	@Override
	protected void writeNBT(NBTTagCompound nbt) {
		nbt.setBoolean("multipleInputs", multipleInputs);
		nbt.setInteger("bufferType", main.bufferType.ordinal());
		NBTTagCompound m = new NBTTagCompound();
		NBTTagCompound p = new NBTTagCompound();
		NBTTagCompound s = new NBTTagCompound();
		main.writeToNBT(m);
		primary.writeToNBT(p);
		secondary.writeToNBT(s);
		nbt.setCompoundTag("main", m);
		nbt.setCompoundTag("primary", p);
		nbt.setCompoundTag("secondary", s);
		nbt.setCompoundTag("faces", writeFacingNBT());
	}
	
	// #################################################################
	// ISidedInventory
	
	@Override public int getSizeInventory(){ return 0; }
	@Override public ItemStack getStackInSlotOnClosing(int slot){ return null; }
	@Override public String getInvName(){ return "__prioritizer__"; } // is this even used outside stupid vanilla code?
	@Override public boolean isInvNameLocalized(){ return false; }
	@Override public int getInventoryStackLimit(){ return 64; }
	@Override public boolean isUseableByPlayer(EntityPlayer entityplayer){ return false; }
	@Override public void openChest(){}
	@Override public void closeChest(){}
	@Override public boolean isItemValidForSlot(int i, ItemStack itemstack){ return true; }
	
	@Override
	public ItemStack getStackInSlot(int slot) {
		if(slot <  8) return ((BufferItem)main).getContainer().getStackInSlot(slot);
		if(slot < 16) return ((BufferItem)primary).getContainer().getStackInSlot(slot - 8);
		if(slot < 24) return ((BufferItem)secondary).getContainer().getStackInSlot(slot - 16);
		return null;
	}

	@Override
	public ItemStack decrStackSize(int slot, int size) {
		if(slot <  8) return ((BufferItem)main).getContainer().decrStackSize(slot, size);
		if(slot < 16) return ((BufferItem)primary).getContainer().decrStackSize(slot - 8, size);
		if(slot < 24) return ((BufferItem)secondary).getContainer().decrStackSize(slot - 16, size);
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		if(slot <  8){
			((BufferItem)main).getContainer().setInventorySlotContents(slot, stack);
			return;
		}
		if(slot < 16){
			((BufferItem)primary).getContainer().setInventorySlotContents(slot - 8, stack);
			return;
		}
		if(slot < 24){
			((BufferItem)secondary).getContainer().setInventorySlotContents(slot - 16, stack);
			return;
		}
	}
	
	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		if(main.bufferType != BufferType.ITEM) return new int[]{};
		FaceColor color = getColor(side);
		if(color.isMain()) return new int[]{0, 1, 2, 3, 4, 5, 6, 7};
		if(color.isPrimary()) return new int[]{8, 9, 10, 11, 12, 13, 14, 15};
		if(color.isSecondary()) return new int[]{16, 17, 18, 19, 20, 21, 22, 23};
		return new int[]{};
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		LinkedList<Integer> slots = new LinkedList<Integer>();
		for(int i : getAccessibleSlotsFromSide(side)) slots.add(i);
		return getColor(side).isInput() && slots.contains(slot);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		LinkedList<Integer> slots = new LinkedList<Integer>();
		for(int i : getAccessibleSlotsFromSide(side)) slots.add(i);
		return getColor(side).isOutput() && slots.contains(slot);
	}
	
	// #################################################################
	// IEnergyHandler
	
	private IEnergyStorage getCapacitor(ForgeDirection fd){
		if(main.bufferType != BufferType.ENERGY) return null;
		FaceColor color = getColor(fd);
		if(color.isMain()) return ((BufferEnergy)main).getContainer();
		if(color.isPrimary()) return ((BufferEnergy)primary).getContainer();
		if(color.isSecondary()) return ((BufferEnergy)secondary).getContainer();
		return null;
	}
	
	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		IEnergyStorage capacitor = getCapacitor(from);
		if(capacitor != null && getColor(from).isInput()){
			return capacitor.receiveEnergy(maxReceive, simulate);
		}
		return 0;
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		IEnergyStorage capacitor = getCapacitor(from);
		if(capacitor != null && getColor(from).isOutput()){
			return capacitor.extractEnergy(maxExtract, simulate);
		}
		return 0;
	}

	@Override
	public boolean canInterface(ForgeDirection from) {
		return getCapacitor(from) != null;
	}

	@Override
	public int getEnergyStored(ForgeDirection from) {
		IEnergyStorage capacitor = getCapacitor(from);
		if(capacitor != null){
			return capacitor.getEnergyStored();
		}
		return 0;
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		IEnergyStorage capacitor = getCapacitor(from);
		if(capacitor != null){
			return capacitor.getMaxEnergyStored();
		}
		return 0;
	}
	
	// #################################################################
	// IFluidHandler
	
	private IFluidTank getTank(ForgeDirection fd){
		if(main.bufferType != BufferType.FLUID) return null;
		FaceColor color = getColor(fd);
		if(color.isMain()) return ((BufferFluid)main).getContainer();
		if(color.isPrimary()) return ((BufferFluid)primary).getContainer();
		if(color.isSecondary()) return ((BufferFluid)secondary).getContainer();
		return null;
	}
	
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		IFluidTank tank = getTank(from);
		if(tank != null && getColor(from).isInput()){
			return tank.fill(resource, doFill);
		}
		return 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		IFluidTank tank = getTank(from);
		if(tank != null && getColor(from).isOutput() && tank.getFluid().containsFluid(resource)){
			return tank.drain(resource.amount, doDrain);
		}
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		IFluidTank tank = getTank(from);
		if(tank != null && getColor(from).isOutput()){
			return tank.drain(maxDrain, doDrain);
		}
		return null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		IFluidTank tank = getTank(from);
		if(tank == null) return false;
		FluidStack mf = ((BufferFluid)main).getContainer().getFluid();
		return getColor(from).isInput() && (mf == null || mf.getFluid().getID() == fluid.getID());
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		IFluidTank tank = getTank(from);
		return tank != null && getColor(from).isOutput();
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		IFluidTank tank = getTank(from);
		if(tank != null) return new FluidTankInfo[]{tank.getInfo()};
		return new FluidTankInfo[]{};
	}
}