package stargatetech2.enemy.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stargatetech2.core.base.BaseContainer;
import stargatetech2.enemy.tileentity.TileParticleIonizer;
import stargatetech2.enemy.util.IonizedParticles;

public class ContainerParticleIonizer extends BaseContainer {
	public final TileParticleIonizer ionizer;
	public final EntityPlayer player;
	
	public ContainerParticleIonizer(TileParticleIonizer ionizer, EntityPlayer player){
		this.ionizer = ionizer;
		this.player = player;
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				addSlotToContainer(new Slot(ionizer.solidIonizable, i*3 + j, 62 + j*18, 30 + i*18));
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
				if(!mergeItemStack(stackInSlot, 0, ionizer.getSizeInventory(), false))
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
	
	private int lPower = -1;
	private int lFIonID = 0;
	private int lFIonQt = 0;
	private int lRecipe = -1;
	private int lPIonQt = 0;
	private int lTicks = 0;
	
	@Override
	public void detectAndSendChanges(){
		super.detectAndSendChanges();
		int power = ionizer.energy.getEnergyStored();
		if(power != lPower){
			sendUpdate(0, power);
			lPower = power;
		}
		FluidStack fIon = ionizer.fluidIonizable.getFluid();
		if(fIon == null || fIon.amount == 0 ){
			if(lFIonID != 0){
				sendUpdate(1, 0);
				lFIonID = 0;
			}
			if(lFIonQt != 0){
				sendUpdate(2, 0);
				lFIonQt = 0;
			}			
		}else{
			if(lFIonID != fIon.fluidID){
				sendUpdate(1, fIon.fluidID);
				lFIonID = fIon.fluidID;
			}
			if(lFIonQt != fIon.amount){
				sendUpdate(2, fIon.amount);
				lFIonQt = fIon.amount;
			}
		}
		int currentRecipe = ionizer.getRecipe();
		if(lRecipe != currentRecipe){
			sendUpdate(3, currentRecipe);
			lRecipe = currentRecipe;
		}
		int ions = ionizer.ionizedParticles.getFluidAmount();
		if(ions != lPIonQt){
			sendUpdate(4, ions);
			lPIonQt = ions;
		}
		int ticks = ionizer.getWorkLeft();
		if(lTicks != ticks){
			sendUpdate(5, ticks);
			lTicks = ticks;
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void updateProgressBar(int key, int value){
		switch(key){
			case 0:
				ionizer.energy.setEnergyStored(value);
				break;
			case 1:
				if(value == 0){
					ionizer.fluidIonizable.setFluid(null);
				}else{
					ionizer.fluidIonizable.setFluid(new FluidStack(value, 1));
				}
				break;
			case 2:
				if(value == 0){
					ionizer.fluidIonizable.setFluid(null);
				}else{
					FluidStack fs = ionizer.fluidIonizable.getFluid();
					if(fs != null){
						fs.amount = value;
					}
				}
				break;
			case 3:
				ionizer.setRecipe(value);
				break;
			case 4:
				ionizer.ionizedParticles.setFluid(new FluidStack(IonizedParticles.fluid, value));
				break;
			case 5:
				lTicks = value;
				break;
		}
	}
	
	public int getWorkLeft(){
		return lTicks;
	}
}