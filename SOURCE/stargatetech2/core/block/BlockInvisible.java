package stargatetech2.core.block;

import stargatetech2.common.base.BaseBlock;
import stargatetech2.common.reference.BlockReference;

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
}