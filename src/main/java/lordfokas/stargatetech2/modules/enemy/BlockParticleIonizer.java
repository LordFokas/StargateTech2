package lordfokas.stargatetech2.modules.enemy;

import lordfokas.stargatetech2.modules.core.machine.BlockMachine;
import lordfokas.stargatetech2.modules.core.machine.TileMachine;
import lordfokas.stargatetech2.reference.BlockReference;
import lordfokas.stargatetech2.util.GUIHandler.Screen;

public class BlockParticleIonizer extends BlockMachine{
	public BlockParticleIonizer() {
		super(BlockReference.PARTICLE_IONIZER, false, Screen.PARTICLE_IONIZER);
	}

	@Override
	protected TileMachine createTileEntity(int metadata) {
		return new TileParticleIonizer();
	}
}