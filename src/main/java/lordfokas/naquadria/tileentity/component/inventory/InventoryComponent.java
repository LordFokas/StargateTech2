package lordfokas.naquadria.tileentity.component.inventory;

import lordfokas.naquadria.tileentity.component.CapabilityComponent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.items.IItemHandler;

public class InventoryComponent extends CapabilityComponent<IItemHandler>{
	@CapabilityInject(IItemHandler.class)
	private static Capability itemHandlerCapability = null;
	private Inventory inventory;
	
	public InventoryComponent(int size){
		this.inventory = new Inventory(size);
	}
	
	public Inventory getInventory(){
		return inventory;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt){
		inventory.readFromNBT(nbt);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		return inventory.writeToNBT(nbt);
	}
	
	@Override
	public Capability<IItemHandler> getCapability() {
		return itemHandlerCapability;
	}
	
	@Override
	public IItemHandler getCapability(EnumFacing side){
		return null; // TODO: finish
	}
}