package lordfokas.stargatetech2.modules.enemy;

import lordfokas.stargatetech2.lib.block.BlockMachine;
import lordfokas.stargatetech2.reference.BlockReference;

public class BlockShieldController extends BlockMachine{

	public BlockShieldController() {
		super(BlockReference.SHIELD_CONTROLLER, TileShieldController.class);
	}
	
	/*@Override // We'll use this later to turn shields on and off depending on RS state.
	public void onNeighborBlockChange(World w, int x, int y, int z, Block n) {
		boolean power = w.isBlockIndirectlyGettingPowered(x, y, z);
	}*/
}