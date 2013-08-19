package stargatetech2.common.machine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import stargatetech2.common.base.BaseContainer;
import stargatetech2.common.base.BaseTileEntity;

public class ContainerMachineInventory extends BaseContainer {
	private IInventory inventory;
	
	public ContainerMachineInventory(BaseTileEntity bte){
		super(bte);
		if(!(bte instanceof IInventory)){
			throw new IllegalArgumentException("BaseTileEntity not implementor of IInventory!");
		}else{
			inventory = (IInventory) bte;
		}
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int sid){
		Slot slot = (Slot) inventorySlots.get(sid);
		ItemStack stack = null;
		ItemStack stackInSlot = null;
		if (slot != null && slot.getHasStack()) {
			stackInSlot = slot.getStack();
			stack = stackInSlot.copy();
			if(sid == 36){ // Clicked TE Slot
				if(!mergeItemStack(stackInSlot, 0, 36, true))
					return null;
			}else{ // Clicked Player Inv Slot
				if(!mergeItemStack(stackInSlot, 36, 36+inventory.getSizeInventory(), true))
					return null;
			}
			if(stackInSlot.stackSize == 0) slot.putStack(null);
	        else slot.onSlotChanged();
	        if (stackInSlot.stackSize == stack.stackSize)
	        	return null;
	        slot.onPickupFromSlot(player, stackInSlot);
		}
		return stack;
	}
}