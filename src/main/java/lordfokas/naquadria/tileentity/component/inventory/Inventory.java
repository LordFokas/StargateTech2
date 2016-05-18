package lordfokas.naquadria.tileentity.component.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;

public class Inventory implements IItemHandler, INBTSerializable<NBTTagCompound>{
	private ItemStack[] inventory;
	
	public Inventory(int size){
		inventory = new ItemStack[size];
	}
	
	@Override
	public int getSlots(){
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot){
		return inventory[slot];
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate){
		ItemStack target = inventory[slot];
		if(ItemStack.areItemStacksEqual(target, stack)){
			int max = target.getMaxStackSize();
			if(target.stackSize < max){
				if(target.stackSize + stack.stackSize <= max){
					target.stackSize += stack.stackSize;
					return null;
				}else{
					int split = max - target.stackSize;
					stack.splitStack(split);
					target.stackSize = max;
					return stack;
				}
			}else{
				return stack;
			}
		}else{
			return stack;
		}
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate){
		if(inventory[slot] == null) return null;
		ItemStack stack = ItemStack.copyItemStack(inventory[slot]);
		if(amount > stack.stackSize) amount = stack.stackSize;
		ItemStack extracted = stack.splitStack(amount);
		if(!simulate) inventory[slot] = (stack.stackSize < 1) ? null : stack;
		return extracted;
	}

	@Override
	public NBTTagCompound serializeNBT(){
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("size", inventory.length);
		for(int slot = 0; slot < inventory.length; slot++){
			if(inventory[slot] != null){
				NBTTagCompound stack = new NBTTagCompound();
				inventory[slot].writeToNBT(stack);
				nbt.setTag("slot_" + slot, stack);
			}
		}
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		inventory = new ItemStack[nbt.getInteger("size")];
		for(int slot = 0; slot < inventory.length; slot++){
			if(nbt.hasKey("slot_" + slot)){
				inventory[slot] = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("slot_" + slot));
			}
		}
	}
}