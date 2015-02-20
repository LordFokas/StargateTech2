package lordfokas.stargatetech2.modules.enemy;

import lordfokas.stargatetech2.ZZ_THRASH.BlockMachine__THRASH;
import lordfokas.stargatetech2.reference.BlockReference;
import lordfokas.stargatetech2.util.GUIHandler.Screen;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockShieldController extends BlockMachine__THRASH {

	public BlockShieldController() {
		super(BlockReference.SHIELD_CONTROLLER, true, Screen.SHIELD_CONTROLLER);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileShieldController();
	}
	
	/*@Override // We'll use this later to turn shields on and off depending on RS state.
	public void onNeighborBlockChange(World w, int x, int y, int z, Block n) {
		boolean power = w.isBlockIndirectlyGettingPowered(x, y, z);
	}*/
}