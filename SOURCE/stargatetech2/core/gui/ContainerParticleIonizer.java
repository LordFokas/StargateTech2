package stargatetech2.core.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import stargatetech2.common.machine.ContainerMachineInventory;
import stargatetech2.core.tileentity.TileParticleIonizer;

public class ContainerParticleIonizer extends ContainerMachineInventory{
	public ContainerParticleIonizer(TileParticleIonizer tpi, EntityPlayer player){
		super(tpi);
		bindInventory(player, 16, 112);
		addSlotToContainer(new Slot(tpi, 0, 16, 31));
		addSlotToContainer(new Slot(tpi, 1, 34, 31));
		addSlotToContainer(new Slot(tpi, 2, 52, 31));
		addSlotToContainer(new Slot(tpi, 3, 16, 49));
		addSlotToContainer(new Slot(tpi, 4, 34, 49));
		addSlotToContainer(new Slot(tpi, 5, 52, 49));
		addSlotToContainer(new Slot(tpi, 6, 16, 67));
		addSlotToContainer(new Slot(tpi, 7, 34, 67));
		addSlotToContainer(new Slot(tpi, 8, 52, 67));
	}
}