package lordfokas.stargatetech2.factory.block;

import lordfokas.stargatetech2.core.machine.BlockMachine;
import lordfokas.stargatetech2.core.machine.TileMachine;
import lordfokas.stargatetech2.core.reference.BlockReference;
import lordfokas.stargatetech2.core.util.GUIHandler.Screen;
import lordfokas.stargatetech2.factory.tileentity.TileCrossover;

public class BlockCrossover extends BlockMachine {

	public BlockCrossover() {
		super(BlockReference.CROSSOVER, false, Screen.CROSSOVER);
	}

	@Override
	protected TileMachine createTileEntity(int metadata) {
		return new TileCrossover();
	}
}