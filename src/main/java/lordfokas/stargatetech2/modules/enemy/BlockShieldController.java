package lordfokas.stargatetech2.modules.enemy;

import lordfokas.stargatetech2.modules.core.machine__TRASH.BlockMachine;
import lordfokas.stargatetech2.modules.core.machine__TRASH.TileMachine__THRASH;
import lordfokas.stargatetech2.reference.BlockReference;
import lordfokas.stargatetech2.util.GUIHandler.Screen;

public class BlockShieldController extends BlockMachine {

	public BlockShieldController() {
		super(BlockReference.SHIELD_CONTROLLER, true, Screen.SHIELD_CONTROLLER);
	}
	
	@Override
	protected TileMachine__THRASH createTileEntity(int metadata) {
		return new TileShieldController();
	}
	
	/*@Override // We'll use this later to turn shields on and off depending on RS state.
	public void onNeighborBlockChange(World w, int x, int y, int z, Block n) {
		boolean power = w.isBlockIndirectlyGettingPowered(x, y, z);
	}*/
}