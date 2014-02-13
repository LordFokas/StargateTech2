package stargatetech2.factory.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import stargatetech2.core.machine.FaceColor;
import stargatetech2.core.machine.TileEntityMachine;

public class TileEntityCrossover extends TileEntityMachine {
	@Override
	public FaceColor getColorOnSide(int side) {
		return FaceColor.values()[side];
	}

	@Override
	protected void readNBT(NBTTagCompound nbt) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void writeNBT(NBTTagCompound nbt) {
		// TODO Auto-generated method stub

	}

}
