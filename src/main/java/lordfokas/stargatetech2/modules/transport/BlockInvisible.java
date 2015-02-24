package lordfokas.stargatetech2.modules.transport;

import lordfokas.stargatetech2.lib.block.BaseBlock;
import lordfokas.stargatetech2.reference.BlockReference;
import net.minecraft.world.IBlockAccess;

public class BlockInvisible extends BaseBlock {

	public BlockInvisible() {
		super(BlockReference.INVISIBLE);
		setRenderer(null);
		setCreativeTab(null);
	}
	
	@Override
	public boolean isOpaqueCube(){
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		return (world.getBlockMetadata(x, y, z) & 15);
	}
	
}