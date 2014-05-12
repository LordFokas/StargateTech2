package stargatetech2.enemy.block;

import net.minecraft.world.World;
import stargatetech2.core.machine.BlockMachine;
import stargatetech2.core.machine.TileMachine;
import stargatetech2.core.reference.BlockReference;

public class BlockShieldEmitter extends BlockMachine{

	public BlockShieldEmitter() {
		super(BlockReference.SHIELD_EMITTER, true);
	}

	@Override
	protected TileMachine createTileEntity(int metadata) {
		return null;
	}
	
	public boolean canPlaceBlockAt(World w, int x, int y, int z){
		if(!super.canPlaceBlockAt(w, x, y, z)) return false;
		return true; // TODO: implement all other checks.
	}
}