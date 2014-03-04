package stargatetech2.enemy.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import stargatetech2.core.base.BaseContainer;
import stargatetech2.enemy.tileentity.TileParticleIonizer;

public class ContainerParticleIonizer extends BaseContainer {
	public final TileParticleIonizer ionizer;
	public final EntityPlayer player;
	
	public ContainerParticleIonizer(TileParticleIonizer ionizer, EntityPlayer player){
		this.ionizer = ionizer;
		this.player = player;
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				addSlotToContainer(new Slot(ionizer.solidIonizable, i*3 + j, 62 + i*18, 30 + j*18));
			}
		}
		bindInventory(player, 27, 112);
	}
	
	@Override
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