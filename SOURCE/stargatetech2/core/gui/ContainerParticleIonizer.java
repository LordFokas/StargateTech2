package stargatetech2.core.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import stargatetech2.common.base.BaseContainer;
import stargatetech2.core.tileentity.TileParticleIonizer;

public class ContainerParticleIonizer extends BaseContainer{
	public TileParticleIonizer ionizer;
	
	public ContainerParticleIonizer(TileParticleIonizer tpi, EntityPlayer player){
		ionizer = tpi;
		addSlotToContainer(new Slot(tpi, 0, 16, 31));
		addSlotToContainer(new Slot(tpi, 1, 34, 31));
		addSlotToContainer(new Slot(tpi, 2, 52, 31));
		addSlotToContainer(new Slot(tpi, 3, 16, 49));
		addSlotToContainer(new Slot(tpi, 4, 34, 49));
		addSlotToContainer(new Slot(tpi, 5, 52, 49));
		addSlotToContainer(new Slot(tpi, 6, 16, 67));
		addSlotToContainer(new Slot(tpi, 7, 34, 67));
		addSlotToContainer(new Slot(tpi, 8, 52, 67));
		bindInventory(player, 16, 112);
	}
	
	public ItemStack transferStackInSlot(EntityPlayer player, int sid){
		Slot slot = (Slot) inventorySlots.get(sid);
		ItemStack stack = null;
		ItemStack stackInSlot = null;
		if (slot != null && slot.getHasStack()) {
			stackInSlot = slot.getStack();
			stack = stackInSlot.copy();
			if(sid < 9){ // Clicked TE Slot
				if(!mergeItemStack(stackInSlot, 9, 45, true))
					return null;
			}else{ // Clicked Player Inv Slot
				if(!mergeItemStack(stackInSlot, 0, ionizer.getSizeInventory(), true))
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