package lordfokas.stargatetech2.modules.enemy;

import java.util.LinkedList;

import lordfokas.stargatetech2.lib.util.Inventory;
import lordfokas.stargatetech2.modules.ModuleEnemy;
import lordfokas.stargatetech2.modules.core.machine.FaceColor;
import lordfokas.stargatetech2.modules.core.machine.TileMachine;
import lordfokas.stargatetech2.util.api.ParticleIonizerRecipes;
import lordfokas.stargatetech2.util.api.ParticleIonizerRecipes.IonizerRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;

public class TileParticleIonizer extends TileMachine implements IFluidHandler, IEnergyHandler, ISidedInventory{
	public final FluidTank ionizedParticles = new FluidTank(8000);		// orange
	public final FluidTank fluidIonizable = new FluidTank(8000);		// blue
	public final Inventory solidIonizable = new Inventory(9);			// blue
	public final EnergyStorage energy = new EnergyStorage(32000, 400);
	private IonizerRecipe recipe = null;
	private int ticksLeft = 0;
	private long nextSearch = 0;
	
	@Override
	public void invalidate(){
		super.invalidate();
		BlockParticleIonizer block = ModuleEnemy.particleIonizer;
		for(int slot = 0; slot < solidIonizable.getSizeInventory(); slot++){
			ItemStack stack = solidIonizable.getStackInSlot(slot);
			if(stack != null){
				block.dropItemStack(worldObj, xCoord, yCoord, zCoord, stack);
			}
		}
	}
	
	@Override
	public void updateEntity(){
		if(!worldObj.isRemote){
			if(recipe != null){
				work();
			}
			if(recipe == null && worldObj.getTotalWorldTime() >= nextSearch){
				findRecipe();
				nextSearch = worldObj.getTotalWorldTime() + 20;
			}
		}
	}
	
	private void work(){
		if(ticksLeft > 0){
			if(ionizedParticles.getCapacity() - ionizedParticles.getFluidAmount() >= recipe.ions && energy.getEnergyStored() >= recipe.power){
				ionizedParticles.fill(new FluidStack(IonizedParticles.fluid, recipe.ions), true);
				energy.extractEnergy(recipe.power, false);
				ticksLeft--;
			}
		}else{
			FluidStack fluid = fluidIonizable.getFluid();
			ItemStack[] inventory = getInventory();
			if(recipe.checkMatch(fluid, inventory)){
				FluidStack fs = recipe.getFluid();
				if(fs != null){
					fluidIonizable.drain(fs.amount, true);
				}
				ItemStack is = recipe.getSolid();
				if(is != null){
					for(int i = 0; i < solidIonizable.getSizeInventory(); i++){
						ItemStack stack = solidIonizable.getStackInSlot(i);
						if(stack == null) continue;
						stack = stack.copy();
						stack.stackSize = is.stackSize;
						if(ItemStack.areItemStacksEqual(is, stack)){
							solidIonizable.decrStackSize(i, 1);
							break;
						}
					}
				}
				ticksLeft = recipe.time;
			}else{
				recipe = null;
			}
		}
	}
	
	private void findRecipe(){
		FluidStack fluid = fluidIonizable.getFluid();
		ItemStack[] inventory = getInventory();
		LinkedList<IonizerRecipe> solids, fluids, both;
		solids = new LinkedList();
		fluids = new LinkedList();
		both = new LinkedList();
		for(IonizerRecipe r : ParticleIonizerRecipes.recipes().getRecipes()){
			if(r.checkMatch(fluid, inventory)){
				if(r.getFluid() != null && r.getSolid() != null){
					both.add(r);
				}else if(r.getFluid() != null){
					fluids.add(r);
				}else{
					solids.add(r);
				}
			}
		}
		if(!both.isEmpty()){
			pickRecipe(both);
		}else if(!solids.isEmpty()){
			pickRecipe(solids);
		}else if(!fluids.isEmpty()){
			pickRecipe(fluids);
		}
	}
	
	private void pickRecipe(LinkedList<IonizerRecipe> list){
		IonizerRecipe pick = null;
		for(IonizerRecipe r : list){
			if(pick == null || pick.ions < r.ions || (pick.ions == r.ions && pick.power > r.power)){
				pick = r;
			}
		}
		recipe = pick;
		ticksLeft = 0;
	}
	
	private ItemStack[] getInventory(){
		ItemStack[] inventory = new ItemStack[solidIonizable.getSizeInventory()];
		for(int i = 0; i < inventory.length; i++){
			inventory[i] = solidIonizable.getStackInSlot(i);
		}
		return inventory;
	}
	
	public int getWorkLeft(){
		return ticksLeft;
	}
	
	public IonizerRecipe getRecipeInstance(){
		return recipe;
	}
	
	public int getRecipe(){
		if(recipe == null) return -1;
		else return ParticleIonizerRecipes.recipes().getRecipeID(recipe);
	}
	
	public void setRecipe(int r){
		recipe = ParticleIonizerRecipes.recipes().getRecipe(r);
	}
	
	@Override
	protected void readNBT(NBTTagCompound nbt){
		ionizedParticles.readFromNBT(nbt.getCompoundTag("ionizedParticles"));
		fluidIonizable.readFromNBT(nbt.getCompoundTag("fluidIonizable"));
		solidIonizable.readFromNBT(nbt.getCompoundTag("solidIonizable"));
		energy.readFromNBT(nbt.getCompoundTag("energy"));
		readFacingNBT(nbt.getCompoundTag("facing"));
		setRecipe(nbt.getInteger("recipe"));
		ticksLeft = nbt.getInteger("ticksLeft");
		nextSearch = nbt.getLong("nextSearch");
	}
	
	@Override
	protected void writeNBT(NBTTagCompound nbt){
		nbt.setTag("ionizedParticles", ionizedParticles.writeToNBT(new NBTTagCompound()));
		nbt.setTag("fluidIonizable", fluidIonizable.writeToNBT(new NBTTagCompound()));
		nbt.setTag("solidIonizable", solidIonizable.writeToNBT(new NBTTagCompound()));
		nbt.setTag("energy", energy.writeToNBT(new NBTTagCompound()));
		nbt.setTag("facing", writeFacingNBT());
		nbt.setInteger("recipe", getRecipe());
		nbt.setInteger("ticksLeft", ticksLeft);
		nbt.setLong("nextSearch", nextSearch);
	}
	
	@Override
	protected FaceColor[] getPossibleFaceColors() {
		return new FaceColor[]{FaceColor.VOID, FaceColor.BLUE, FaceColor.ORANGE, FaceColor.STRIPES};
	}
	
	// ############################################################################################
	// IFluidHandler
	@Override
	public int fill(ForgeDirection side, FluidStack resource, boolean doFill) {
		if(getColor(side).isInput() && ParticleIonizerRecipes.recipes().isIonizable(resource)){
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
		if(getColor(side).isOutput()){
			return ionizedParticles.drain(maxDrain, doDrain);
		}
		return null;
	}

	@Override
	public boolean canFill(ForgeDirection side, Fluid fluid) {
		return getColor(side).isInput();
	}

	@Override
	public boolean canDrain(ForgeDirection side, Fluid fluid) {
		return getColor(side).isOutput();
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection side) {
		FaceColor color = getColor(side);
		if(color.isInput() && color.isOutput()){
			return new FluidTankInfo[]{fluidIonizable.getInfo(), ionizedParticles.getInfo()};
		}else if(color.isInput()){
			return new FluidTankInfo[]{fluidIonizable.getInfo()};
		}else if(color.isOutput()){
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
	public int getEnergyStored(ForgeDirection side) {
		return energy.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection side) {
		return energy.getMaxEnergyStored();
	}
	
	@Override
	public boolean canConnectEnergy(ForgeDirection from) {
		return true;
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
		return ParticleIonizerRecipes.recipes().isIonizable(stack);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		if(getColor(side).isInput()){
			return new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
		}
		return new int[]{};
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return getColor(side).isInput() && solidIonizable.canInsert();
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return getColor(side).isInput() && solidIonizable.canExtract();
	}
	
	// useless stuff...  :c  (creeper face!)
	@Override public boolean isUseableByPlayer(EntityPlayer entityplayer){ return true; }
	@Override public int getInventoryStackLimit(){ return 64; }
	@Override public ItemStack getStackInSlotOnClosing(int slot){ return null; }
	@Override public String getInventoryName(){ return "Particle Ionizer"; }
	@Override public boolean hasCustomInventoryName(){ return false; }
	@Override public void openInventory(){}
	@Override public void closeInventory(){}
}