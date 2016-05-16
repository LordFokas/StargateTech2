package lordfokas.stargatetech2.lib.tileentity.component.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IChatComponent;

// TODO: overhaul this?
public class Inventory{
	private ItemStack[] inventory;
	private InventoryMode mode;
	
	public static enum InventoryMode{
		INPUT, OUTPUT, BOTH;
		
		public boolean canReceive(){
			return this != OUTPUT;
		}
		
		public boolean canExtract(){
			return this != INPUT;
		}
	}
	
	public Inventory(int size){
		this(size, InventoryMode.BOTH);
	}
	
	public Inventory(int size, InventoryMode mode){
		inventory = new ItemStack[size];
		this.mode = mode;
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound inventoryNBT){
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
		for(int slot = 0; slot < inventory.length; slot++){
			if(inventoryNBT.hasKey("slot_" + slot)){
				inventory[slot] = ItemStack.loadItemStackFromNBT(inventoryNBT.getCompoundTag("slot_" + slot));
			}
		}
	}
	
	public int getSlotCount() {
		return inventory.length;
	}
	
	public ItemStack getStack(int slot) {
		return inventory[slot];
	}
	
	public ItemStack removeStack(int index){
		if(!canExtract()) return null;
		ItemStack stack = inventory[index];
		inventory[index] = null;
		return stack;
	}
	
	public ItemStack decreaseStack(int slot, int amount) {
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
	
	public void setStack(int slot, ItemStack stack) {
		inventory[slot] = stack;
	}
	
	public boolean canInsert(){
		return mode.canReceive();
	}
	
	public boolean canExtract(){
		return mode.canExtract();
	}
}