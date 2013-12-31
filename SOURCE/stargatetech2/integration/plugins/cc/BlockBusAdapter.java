package stargatetech2.integration.plugins.cc;

import stargatetech2.common.base.BaseBlockContainer;
import stargatetech2.common.base.BaseTileEntity;
import stargatetech2.common.reference.BlockReference;

public class BlockBusAdapter extends BaseBlockContainer {
	
	public BlockBusAdapter() {
		super(BlockReference.BUS_ADAPTER);
		
	}

	@Override
	protected BaseTileEntity createTileEntity(int metadata) {
		return new TileBusAdapter();
	}
}