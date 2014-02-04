package stargatetech2.enemy.block;

import net.minecraft.world.IBlockAccess;
import stargatetech2.core.machine.BlockMachine;
import stargatetech2.core.reference.BlockReference;
import stargatetech2.core.reference.TextureReference;
import stargatetech2.core.util.GUIHandler.Screen;
import stargatetech2.enemy.tileentity.TileParticleIonizer;

public class BlockParticleIonizer extends BlockMachine{

	public BlockParticleIonizer() {
		super(BlockReference.PARTICLE_IONIZER, Screen.PARTICLE_IONIZER);
		setIsAbstractBusBlock();
	}

	@Override
	protected TileParticleIonizer createTileEntity(int metadata) {
		return new TileParticleIonizer();
	}

	@Override
	public String getFace(IBlockAccess world, int x, int y, int z) {
		return TextureReference.FACE_PARTICLE_IONIZER;
	}

	@Override
	public String getGlow(IBlockAccess world, int x, int y, int z) {
		return TextureReference.GLOW_PARTICLE_IONIZER;
	}
}