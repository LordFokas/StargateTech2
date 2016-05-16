package lordfokas.naquadria.tileentity.component.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.IItemHandler;

public class Inventory{
	private ItemStack[] inventory;
	
	public Inventory(int size){
		inventory = new ItemStack[size];
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound inventoryNBT){
		inventoryNBT.setInteger("size", inventory.length);
		for(int slot = 0; slot < inventory.length; slot++){
			if(inventory[slot] != null){
				NBTTagCompound stack = new NBTTagCompound();
				inventory[slot].writeToNBT(stack);
				inventoryNBT.setTag("slot_" + slot, stack);
			}
		}
		return inventoryNBT;
	}
	
	public void readFromNBT(NBTTagCompound inventoryNBT){
		inventory = new ItemStack[inventoryNBT.getInteger("size")];
		for(int slot = 0; slot < inventory.length; slot++){
			if(inventoryNBT.hasKey("slot_" + slot)){
				inventory[slot] = ItemStack.loadItemStackFromNBT(inventoryNBT.getCompoundTag("slot_" + slot));
			}
		}
	}
}