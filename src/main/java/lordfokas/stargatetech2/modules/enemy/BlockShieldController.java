package lordfokas.stargatetech2.modules.enemy;

import lordfokas.stargatetech2.lib.block.BlockMachine;
import lordfokas.stargatetech2.reference.BlockReference;
import lordfokas.stargatetech2.util.GUIHandler.Screen;

public class BlockShieldController extends BlockMachine{
	public BlockShieldController() {
		super(BlockReference.SHIELD_CONTROLLER, TileShieldController.class, Screen.SHIELD_CONTROLLER);
	}
}