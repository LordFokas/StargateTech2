package stargatetech2.transport.block;

import stargatetech2.core.base.BaseBlock;
import stargatetech2.core.reference.BlockReference;

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