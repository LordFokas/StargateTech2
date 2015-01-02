package lordfokas.stargatetech2.transport;

import lordfokas.stargatetech2.core.base.BaseBlock;
import lordfokas.stargatetech2.core.reference.BlockReference;
import net.minecraft.world.IBlockAccess;

public class BlockInvisible extends BaseBlock {

	public BlockInvisible() {
		super(BlockReference.INVISIBLE);
		this.setCreativeTab(null);
	}
	
	@Override
	public boolean isOpaqueCube(){
		return false;
	}
	
	@Override
	public int getRenderType(){
		return -1;
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