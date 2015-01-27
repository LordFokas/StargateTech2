package lordfokas.stargatetech2.modules.enemy;

import lordfokas.stargatetech2.modules.core.machine__TRASH.BlockMachine;
import lordfokas.stargatetech2.modules.core.machine__TRASH.TileMachine__THRASH;
import lordfokas.stargatetech2.reference.BlockReference;
import lordfokas.stargatetech2.util.GUIHandler.Screen;

public class BlockParticleIonizer extends BlockMachine{
	public BlockParticleIonizer() {
		super(BlockReference.PARTICLE_IONIZER, false, Screen.PARTICLE_IONIZER);
	}

	@Override
	protected TileMachine__THRASH createTileEntity(int metadata) {
		return new TileParticleIonizer();
	}
}