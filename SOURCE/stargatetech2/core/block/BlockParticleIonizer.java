package stargatetech2.core.block;

import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import stargatetech2.common.machine.BlockMachine;
import stargatetech2.common.reference.BlockReference;
import stargatetech2.common.reference.TextureReference;
import stargatetech2.common.util.IconRegistry;
import stargatetech2.common.util.GUIHandler.Screen;
import stargatetech2.core.tileentity.TileParticleIonizer;

public class BlockParticleIonizer extends BlockMachine{

	public BlockParticleIonizer() {
		super(BlockReference.PARTICLE_IONIZER, Screen.PARTICLE_IONIZER);
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