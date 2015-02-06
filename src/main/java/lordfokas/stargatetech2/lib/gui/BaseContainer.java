package lordfokas.stargatetech2.lib.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lordfokas.stargatetech2.lib.tileentity.BaseTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class BaseContainer extends Container {
	public final BaseTileEntity te;
	private int[] lastValues = null;
	
	@Deprecated
	public BaseContainer(){
		this(null);
	}
	
	// TODO: remove null checks after finishing refactoring.
	public BaseContainer(BaseTileEntity te){
		this.te = te;
		if(te != null){
			int values = te.getValueCount();
			if(values > 0){
				lastValues = new int[values];
				for(int i = 0; i < values; i++){
					lastValues[i] = Integer.MIN_VALUE;
				}
			}
		}
	}
	
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
	
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if(te != null && lastValues != null){
			for(int i = 0; i < te.getValueCount(); i++){
				int val = te.getValue(i);
				if(val != lastValues[i]){
					sendUpdate(i, val);
					lastValues[i] = val;
				}
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void updateProgressBar(int key, int value){
		te.setValue(key, value);
	}
}