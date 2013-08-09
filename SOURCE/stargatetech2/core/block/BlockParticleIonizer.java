package stargatetech2.core.block;

import stargatetech2.common.base.BaseBlockContainer;
import stargatetech2.common.base.BaseTileEntity;
import stargatetech2.common.reference.BlockReference;
import stargatetech2.core.tileentity.TileParticleIonizer;

public class BlockParticleIonizer extends BaseBlockContainer {

	public BlockParticleIonizer() {
		super(BlockReference.PARTICLE_IONIZER);
	}

	@Override
	protected TileParticleIonizer createTileEntity(int metadata) {
		return new TileParticleIonizer();
	}

}
