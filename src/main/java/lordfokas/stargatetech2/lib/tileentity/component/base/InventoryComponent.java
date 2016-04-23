package lordfokas.stargatetech2.lib.tileentity.component.base;

import lordfokas.stargatetech2.lib.tileentity.component.SidedComponent;
import lordfokas.stargatetech2.lib.tileentity.component.access.IInventoryComponent;
import lordfokas.stargatetech2.lib.util.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class InventoryComponent extends SidedComponent implements IInventoryComponent{
	protected Inventory inventory;
	
	public InventoryComponent(Inventory inventory){
		this.inventory = inventory;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt){
		inventory.readFromNBT(nbt);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		return inventory.writeToNBT(nbt);
	}
	
	@Override
	public int getSlotCount(){
		return inventory.getSizeInventory();
	}
	
	@Override
	public ItemStack getStackInSlot(int slot){
		return inventory.getStackInSlot(slot);
	}
	
	@Override
	public ItemStack decrStackSize(int slot, int amount){
		return inventory.decrStackSize(slot, amount);
	}
	
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack){
		inventory.setInventorySlotContents(slot, stack);
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack){
		return inventory.isItemValidForSlot(slot, stack);
	}
	
	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		if(!inventory.canInsert()) return false;
		ForgeDirection dir = ForgeDirection.getOrientation(side);
		return canInputOnSide(dir) || accessibleOnSide(dir);
	}
	
	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		if(!inventory.canExtract()) return false;
		ForgeDirection dir = ForgeDirection.getOrientation(side);
		return canOutputOnSide(dir) || accessibleOnSide(dir);
	}
}