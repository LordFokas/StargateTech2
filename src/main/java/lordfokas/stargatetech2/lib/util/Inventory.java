package lordfokas.stargatetech2.lib.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IChatComponent;

/**
 * A basic Inventory implementation. It respects IInventory's
 * general contract and supplies additional functionality.
 * 
 * You can simply direct your IInventory calls to it.
 * 
 * @author LordFokas
 */
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
	public ItemStack removeStackFromSlot(int index){
		if(!canExtract()) return null;
		ItemStack stack = inventory[index];
		inventory[index] = null;
		return stack;
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
	
	@Override // TODO: this one is for a future upgrade :)
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return true;
	}
	
	public boolean canInsert(){
		return mode.canReceive();
	}
	
	public boolean canExtract(){
		return mode.canExtract();
	}
	
	// #######################################################################################
	// These don't really matter...
	
	@Override public boolean isUseableByPlayer(EntityPlayer entityplayer) { return false; }
	@Override public int getInventoryStackLimit(){ return 64; }
	@Override public void markDirty(){}
	@Override public String getName() { return "sgtech2.inventory"; }
	@Override public boolean hasCustomName(){ return false; }
	@Override public IChatComponent getDisplayName(){ return null; }
	@Override public void openInventory(EntityPlayer player){}
	@Override public void closeInventory(EntityPlayer player){}

	// XXX: WTF are these, even?
	@Override public int getField(int id){ return 0; }
	@Override public void setField(int id, int value){}
	@Override public int getFieldCount(){ return 0; }
	@Override public void clear(){}
}