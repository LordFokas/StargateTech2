package stargatetech2.factory.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import stargatetech2.core.machine.FaceColor;
import stargatetech2.core.machine.Inventory;
import stargatetech2.core.machine.TileMachine;

public class TileCrossover extends TileMachine {
	private Inventory inventory = new Inventory(3);
	
	
	@Override
	protected void readNBT(NBTTagCompound nbt) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void writeNBT(NBTTagCompound nbt) {
		// TODO Auto-generated method stub

	}

	@Override
	protected FaceColor[] getPossibleFaceColors() {
		return new FaceColor[]{FaceColor.VOID, FaceColor.RED, FaceColor.GREEN, FaceColor.BLUE};
	}

}
