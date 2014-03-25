package stargatetech2.enemy.block;

import stargatetech2.core.machine.BlockMachine;
import stargatetech2.core.machine.TileMachine;
import stargatetech2.core.reference.BlockReference;
import stargatetech2.core.util.GUIHandler.Screen;
import stargatetech2.enemy.tileentity.TileShieldController;

public class BlockShieldController extends BlockMachine {

	public BlockShieldController() {
		super(BlockReference.SHIELD_CONTROLLER, true, Screen.SHIELD_CONTROLLER);
	}
	
	@Override
	protected TileMachine createTileEntity(int metadata) {
		return new TileShieldController();
	}
}