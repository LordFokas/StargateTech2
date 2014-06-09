package stargatetech2.factory.util;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyStorage;
import net.minecraft.nbt.NBTTagCompound;

public class BufferEnergy extends Buffer<BufferEnergy, IEnergyStorage> {
	private EnergyStorage storage;
	
	public BufferEnergy(int energy) {
		super(BufferType.ENERGY);
		storage = new EnergyStorage(energy);
	}
	
	@Override
	public void transferTo(BufferEnergy buffer) {
		storage.extractEnergy(buffer.storage.receiveEnergy(storage.getEnergyStored(), false), false);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		storage.writeToNBT(nbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		storage.readFromNBT(nbt);
	}

	@Override
	public float getFill() {
		return ((float)storage.getEnergyStored()) / ((float)storage.getMaxEnergyStored());
	}

	@Override
	public IEnergyStorage getContainer() {
		return storage;
	}
}
