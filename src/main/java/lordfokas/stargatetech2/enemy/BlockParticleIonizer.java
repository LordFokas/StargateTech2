package lordfokas.stargatetech2.enemy;

import lordfokas.stargatetech2.core.GUIHandler.Screen;
import lordfokas.stargatetech2.core.machine.BlockMachine;
import lordfokas.stargatetech2.core.machine.TileMachine;
import lordfokas.stargatetech2.core.reference.BlockReference;

public class BlockParticleIonizer extends BlockMachine{
	public BlockParticleIonizer() {
		super(BlockReference.PARTICLE_IONIZER, false, Screen.PARTICLE_IONIZER);
	}

	@Override
	protected TileMachine createTileEntity(int metadata) {
		return new TileParticleIonizer();
	}
}