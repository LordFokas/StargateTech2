package lordfokas.stargatetech2.lib.tileentity.component.base;

import lordfokas.stargatetech2.lib.tileentity.component.SidedComponent;
import lordfokas.stargatetech2.lib.tileentity.component.access.IInventoryComponent;
import lordfokas.stargatetech2.lib.util.Inventory;
import net.minecraft.nbt.NBTTagCompound;

public class InventoryComponent extends SidedComponent implements IInventoryComponent{
	protected Inventory inventory;
	
	public InventoryComponent(Inventory inventory){
		this.inventory = inventory;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		inventory.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		return inventory.writeToNBT(nbt);
	}
}