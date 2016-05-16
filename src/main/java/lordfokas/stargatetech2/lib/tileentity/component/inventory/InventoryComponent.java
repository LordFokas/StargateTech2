package lordfokas.stargatetech2.lib.tileentity.component.inventory;

import lordfokas.stargatetech2.lib.tileentity.component.CapabilityComponent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.items.IItemHandler;

public class InventoryComponent extends CapabilityComponent<IItemHandler>{
	@CapabilityInject(IItemHandler.class)
	private static Capability itemHandlerCapability = null;
	protected Inventory inventory;
	
	public InventoryComponent(Inventory inventory){
		this.inventory = inventory;
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
	public IItemHandler getCapability(EnumFacing side){
		return null;
	}
	
	@Override
	public Capability<IItemHandler> getCapability() {
		return itemHandlerCapability;
	}
}