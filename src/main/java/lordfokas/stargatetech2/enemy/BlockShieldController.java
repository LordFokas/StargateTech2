package lordfokas.stargatetech2.enemy;

import lordfokas.stargatetech2.core.GUIHandler.Screen;
import lordfokas.stargatetech2.core.machine.BlockMachine;
import lordfokas.stargatetech2.core.machine.TileMachine;
import lordfokas.stargatetech2.core.reference.BlockReference;

public class BlockShieldController extends BlockMachine {

	public BlockShieldController() {
		super(BlockReference.SHIELD_CONTROLLER, true, Screen.SHIELD_CONTROLLER);
	}
	
	@Override
	protected TileMachine createTileEntity(int metadata) {
		return new TileShieldController();
	}
	
	/*@Override // We'll use this later to turn shields on and off depending on RS state.
	public void onNeighborBlockChange(World w, int x, int y, int z, Block n) {
		boolean power = w.isBlockIndirectlyGettingPowered(x, y, z);
	}*/
}