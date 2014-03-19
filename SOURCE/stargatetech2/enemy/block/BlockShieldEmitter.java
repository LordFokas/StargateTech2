package stargatetech2.enemy.block;

import stargatetech2.core.machine.BlockMachine;
import stargatetech2.core.machine.TileEntityMachine;
import stargatetech2.core.reference.BlockReference;

public class BlockShieldEmitter extends BlockMachine{

	public BlockShieldEmitter() {
		super(BlockReference.SHIELD_EMITTER, true);
	}

	@Override
	protected TileEntityMachine createTileEntity(int metadata) {
		return null;
	}
}