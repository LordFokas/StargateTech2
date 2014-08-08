package lordfokas.stargatetech2.core.machine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class Inventory implements IInventory{
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
	
	@Override
	public int getSizeInventory() {
		return inventory.length;
	}

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

	@Override // this one is for a future upgrade :)
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return true;
	}
	
	// #######################################################################################
	// These don't really matter...
	@Override public boolean isUseableByPlayer(EntityPlayer entityplayer) { return false; }
	@Override public ItemStack getStackInSlotOnClosing(int slot){ return null; }
	@Override public int getInventoryStackLimit(){ return 64; }
	@Override public boolean hasCustomInventoryName(){ return false; }
	@Override public void markDirty(){}
	@Override public void openInventory(){}
	@Override public void closeInventory(){}
	// #######################################################################################
	
	public boolean canInsert(){
		return mode.canReceive();
	}
	
	public boolean canExtract(){
		return mode.canExtract();
	}

	@Override
	public String getInventoryName() {
		return "sgtech2.inventory";
	}
}