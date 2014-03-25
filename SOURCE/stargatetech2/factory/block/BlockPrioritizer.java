package stargatetech2.factory.block;

import stargatetech2.core.machine.BlockMachine;
import stargatetech2.core.machine.TileMachine;
import stargatetech2.core.reference.BlockReference;
import stargatetech2.core.util.GUIHandler.Screen;
import stargatetech2.factory.tileentity.TilePrioritizer;

public class BlockPrioritizer extends BlockMachine {

	public BlockPrioritizer() {
		super(BlockReference.PRIORITIZER, false, Screen.PRIORITIZER);
	}

	@Override
	protected TileMachine createTileEntity(int metadata) {
		return new TilePrioritizer();
	}
}