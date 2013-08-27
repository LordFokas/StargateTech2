package stargatetech2.core.tileentity;

import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;
import net.minecraft.entity.item.EntityItem;
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
import stargatetech2.core.util.ParticleIonizerRecipes.Recipe;

public class TileParticleIonizer extends BaseTileEntity implements IFluidHandler, IPowerReceptor, IInventory{
	private FluidTank tank = new FluidTank(12000);
	private ItemStack[] inventory = new ItemStack[9];
	private PowerHandler powerHandler = new PowerHandler(this, Type.MACHINE);
	public ItemStack consuming = null;
	public int workTicks;
	
	public Recipe recipe;
	
	public TileParticleIonizer(){
		powerHandler.configure(50, 100, 5, 16000);
		powerHandler.configurePowerPerdition(0, 0);
	}
	
	@Override
	public void invalidate(){
		super.invalidate();
		if(worldObj.isRemote == false){
			double x = ((double)xCoord) + 0.5D;
			double y = ((double)yCoord) + 0.5D;
			double z = ((double)zCoord) + 0.5D;
			for(ItemStack stack : inventory){
				if(stack != null){
					worldObj.spawnEntityInWorld(new EntityItem(worldObj, x, y, z,stack));
				}
			}
		}
	}
	
	@Override
	public void updateEntity(){
		if(worldObj.isRemote == false && worldObj.getWorldTime() % 20 == 0){
			if(consuming == null){
				for(int slot = 0; slot < inventory.length; slot++){
					ItemStack item = inventory[slot];
					recipe = ParticleIonizerRecipes.getRecipe(item);
					if(item != null && recipe != null){
						consuming = item.copy();
						item.stackSize--;
						consuming.stackSize = 1;
						if(item.stackSize == 0){
							inventory[slot] = null;
						}
						workTicks = recipe.ticks;
						break;
					}
				}
			}else{
				FluidStack fs = new FluidStack(IonizedParticles.fluid, recipe.ions);
				int fill = tank.fill(fs, false);
				if(fill < fs.amount || powerHandler.useEnergy(recipe.power, recipe.power, false) < recipe.power){
					return;
				}
				powerHandler.useEnergy(recipe.power, recipe.power, true);
				tank.fill(fs, true);
				workTicks--;
				if(workTicks == 0){
					consuming = null;
				}
			}
		}
	}
	
	public int getIonAmount(){
		FluidStack fs = tank.getInfo().fluid;
		return (fs == null) ? 0 : fs.amount;
	}
	
	public void setIonAmount(int value){
		tank.setFluid(new FluidStack(IonizedParticles.fluid, value));
	}
	
	public void setPower(int value){
		powerHandler.setEnergy(value);
	}
	
	@Override
	protected void readNBT(NBTTagCompound nbt) {
		tank.readFromNBT(nbt);
		powerHandler.readFromNBT(nbt.getCompoundTag("powerHandler"));
		for(int slot = 0; slot < inventory.length; slot++){
			if(nbt.hasKey("stack" + slot)){
				inventory[slot] = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("stack" + slot));
			}else{
				inventory[slot] = null;
			}
		}
		if(nbt.hasKey("consuming")){
			consuming = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("consuming"));
			recipe = ParticleIonizerRecipes.getRecipe(consuming);
			if(recipe == null) consuming = null;
		}
		workTicks = nbt.getInteger("workTicks");
	}
	
	@Override
	protected void writeNBT(NBTTagCompound nbt) {
		tank.writeToNBT(nbt);
		NBTTagCompound ph = new NBTTagCompound();
		powerHandler.writeToNBT(ph);
		nbt.setCompoundTag("powerHandler", ph);
		for(int slot = 0; slot < inventory.length; slot++){
			NBTTagCompound stack = new NBTTagCompound();
			if(inventory[slot] != null){
				inventory[slot].writeToNBT(stack);
			}
			nbt.setCompoundTag("stack" + slot, stack);
		}
		if(consuming != null){
			NBTTagCompound stack = new NBTTagCompound();
			consuming.writeToNBT(stack);
			nbt.setCompoundTag("consuming", stack);
		}
		nbt.setInteger("workTicks", workTicks);
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
		if(inventory[slot] != null){
			ItemStack stack;
			if(inventory[slot].stackSize <= amount){
				stack = inventory[slot];
				inventory[slot] = null;
			}else{
				stack = inventory[slot].splitStack(amount);
				if(inventory[slot].stackSize == 0){
					inventory[slot] = null;
				}
			}
			return stack;
		}else{
			return null;
		}
	}
	
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		inventory[slot] = stack;
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return ParticleIonizerRecipes.getRecipe(stack) != null;
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int slot){
		ItemStack stack = inventory[slot];
		inventory[slot] = null;
		return stack;
	}
	
	@Override public String getInvName(){ return "Particle Ionizer"; }
	@Override public boolean isInvNameLocalized(){ return true; }
	@Override public int getInventoryStackLimit(){ return 64; }
	@Override public boolean isUseableByPlayer(EntityPlayer entityplayer){ return true; }
	@Override public int getSizeInventory(){ return inventory.length; }
	@Override public void openChest(){}
	@Override public void closeChest(){}
}