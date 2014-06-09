package stargatetech2.factory.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;

public class BufferFluid extends Buffer<BufferFluid, IFluidTank> {
	private FluidTank tank;
	
	public BufferFluid(int fluid) {
		super(BufferType.FLUID);
		tank = new FluidTank(fluid);
	}

	@Override
	public void transferTo(BufferFluid buffer) {
		tank.drain(buffer.tank.fill(tank.getFluid(), true), true);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		tank.writeToNBT(nbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		tank.readFromNBT(nbt);
	}

	@Override
	public float getFill() {
		return ((float)tank.getFluidAmount()) / ((float)tank.getCapacity());
	}

	@Override
	public IFluidTank getContainer() {
		return tank;
	}
}
