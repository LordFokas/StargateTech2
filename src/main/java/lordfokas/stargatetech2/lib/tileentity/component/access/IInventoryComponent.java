package lordfokas.stargatetech2.lib.tileentity.component.access;

import lordfokas.stargatetech2.lib.tileentity.component.IAccessibleTileComponent;
import net.minecraft.item.ItemStack;

public interface IInventoryComponent extends IAccessibleTileComponent{
	public int getSlotCount();
	public ItemStack getStackInSlot(int slot);
	public ItemStack decrStackSize(int slot, int amount);
	public void setInventorySlotContents(int slot, ItemStack stack);
	public boolean isItemValidForSlot(int slot, ItemStack stack);
	public boolean canInsertItem(int slot, ItemStack stack, int side);
	public boolean canExtractItem(int slot, ItemStack stack, int side);
}
