package stargatetech2.enemy.block;

import stargatetech2.core.machine.BlockMachine;
import stargatetech2.core.machine.TileMachine;
import stargatetech2.core.reference.BlockReference;
import stargatetech2.core.util.GUIHandler.Screen;
import stargatetech2.enemy.tileentity.TileParticleIonizer;

public class BlockParticleIonizer extends BlockMachine{
	public BlockParticleIonizer() {
		super(BlockReference.PARTICLE_IONIZER, false, Screen.PARTICLE_IONIZER);
	}

	@Override
	protected TileMachine createTileEntity(int metadata) {
		return new TileParticleIonizer();
	}
}