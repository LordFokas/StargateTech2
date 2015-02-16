package lordfokas.stargatetech2.lib.tileentity.component.base;

import lordfokas.stargatetech2.lib.tileentity.component.SidedComponent;
import lordfokas.stargatetech2.lib.tileentity.component.access.ICapacitorComponent;
import net.minecraft.nbt.NBTTagCompound;
import cofh.api.energy.EnergyStorage;

public class CapacitorComponent extends SidedComponent implements ICapacitorComponent{
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