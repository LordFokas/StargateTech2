package stargatetech2.core.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import stargatetech2.common.base.BaseContainer;
import stargatetech2.core.tileentity.TileParticleIonizer;
import stargatetech2.core.util.ParticleIonizerRecipes;
import stargatetech2.core.util.ParticleIonizerRecipes.Recipe;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerParticleIonizer extends BaseContainer{
	public TileParticleIonizer ionizer;
	private int lastTicks = -1;
	private int lastPower = -1;
	private int lastParticles = -1;
	private ItemStack consuming = null;
	
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
	
	@Override
	public void detectAndSendChanges(){
		super.detectAndSendChanges();
		int particles = ionizer.getIonAmount();
		if(particles != lastParticles){
			lastParticles = particles;
			sendUpdate(0, particles);
		}
		int power = (int)ionizer.getPowerReceiver(null).getEnergyStored();
		if(power != lastPower){
			lastPower = power;
			sendUpdate(1, power);
		}
		if(ionizer.consuming != consuming){
			consuming = ionizer.consuming;
			if(consuming != null){
				sendUpdate(2, ParticleIonizerRecipes.getRecipeID(ionizer.recipe));
			}else{
				sendUpdate(2, -1);
			}
		}
		int ticks = ionizer.workTicks;
		if(ticks != lastTicks){
			lastTicks = ticks;
			sendUpdate(3, ticks);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void updateProgressBar(int key, int value){
		switch(key){
			case 0:
				ionizer.setIonAmount(value);
				break;
			case 1:
				ionizer.setPower(value);
				break;
			case 2:
				if(value == -1){
					ionizer.recipe = null;
					ionizer.consuming = null;
				}else{
					Recipe recipe = ParticleIonizerRecipes.getRecipe(value);
					ionizer.recipe = recipe;
					ionizer.consuming = recipe.stack;
				}
				break;
			case 3:
				ionizer.workTicks = value;
				break;
		}
	}
}