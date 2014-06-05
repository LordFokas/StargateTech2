package stargatetech2.transport.block;

import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import stargatetech2.core.base.BaseBlock;
import stargatetech2.core.reference.BlockReference;


public class BlockLight extends BaseBlock {

	public BlockLight() {
		super(BlockReference.LIGHT, true, false);
		setBlockBounds(0, 0, 0, 0, 0, 0);
	}

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		return 15;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public int getRenderType(){
		return -1;
	}
	
	@Override
	public boolean isAirBlock(World world, int x, int y, int z) {
		return true;
	}
	
	@Override
	public boolean isCollidable() {
		return false;
	}
	
	@Override
	public boolean isBlockReplaceable(World world, int x, int y, int z) {
		return true;
	}
}
