package stargatetech2.core.base;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class BaseContainer extends Container {
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}
	
	protected void bindInventory(EntityPlayer player, int xOffset, int yOffset){
		IInventory inventoryPlayer = player.inventory;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventoryPlayer, (j + (i * 9)) + 9, xOffset + j * 18, yOffset + i * 18));
			}
		}
		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventoryPlayer, i, xOffset + i * 18, 58 + yOffset));
		}
	}
	
	protected void sendUpdate(int key, int value){
		for(int i = 0; i < crafters.size(); i++){
			((ICrafting)crafters.get(i)).sendProgressBarUpdate(this, key, value);
		}
	}
}