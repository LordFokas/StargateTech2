package lordfokas.naquadria.tileentity.component.base;

import lordfokas.naquadria.tileentity.component.CapabilityComponent;
import lordfokas.stargatetech2.lib.tileentity.component.access.ICapacitorComponent;
import net.minecraft.nbt.NBTTagCompound;
import cofh.api.energy.EnergyStorage;

public class CapacitorComponent extends CapabilityComponent implements ICapacitorComponent{
	protected EnergyStorage storage;
	
	public CapacitorComponent(EnergyStorage storage){
		this.storage = storage;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		storage.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		return storage.writeToNBT(nbt);
	}
}