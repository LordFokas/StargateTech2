package stargatetech2.factory.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import stargatetech2.core.machine.FaceColor;
import stargatetech2.core.machine.TileMachine;

public class TilePrioritizer extends TileMachine {
	private boolean multipleInputs = true;
	
	@Override
	protected FaceColor[] getPossibleFaceColors() {
		if(multipleInputs){ // multiple inputs, one output.
			return new FaceColor[]{FaceColor.PURPLE, FaceColor.GREEN, FaceColor.ORANGE};
		}else{ // one input, multiple outputs.
			return new FaceColor[]{FaceColor.BLUE, FaceColor.RED, FaceColor.YELLOW};
		}
	}

	@Override
	protected void readNBT(NBTTagCompound nbt) {
		multipleInputs = nbt.getBoolean("multipleInputs");
	}

	@Override
	protected void writeNBT(NBTTagCompound nbt) {
		nbt.setBoolean("multipleInputs", multipleInputs);
	}
}