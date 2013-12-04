package stargatetech2.core.block;

import stargatetech2.common.base.BaseBlockContainer;
import stargatetech2.common.base.BaseTileEntity;
import stargatetech2.common.reference.BlockReference;
import stargatetech2.core.rendering.RenderStargateBlock;
import stargatetech2.core.tileentity.TileStargate;

public class BlockStargate extends BaseBlockContainer {

	public BlockStargate() {
		super(BlockReference.STARGATE);
	}
	
	@Override
	public int getRenderType(){
		return RenderStargateBlock.instance().getRenderId();
	}
	
	@Override
	public boolean isOpaqueCube(){
		return false;
	}
	
	@Override
	protected TileStargate createTileEntity(int metadata) {
		return new TileStargate();
	}
}
