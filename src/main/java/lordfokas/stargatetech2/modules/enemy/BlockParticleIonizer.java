package lordfokas.stargatetech2.modules.enemy;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import lordfokas.stargatetech2.lib.block.BlockMachine;
import lordfokas.stargatetech2.modules.core.machine__TRASH.TileMachine__THRASH;
import lordfokas.stargatetech2.reference.BlockReference;
import lordfokas.stargatetech2.util.GUIHandler.Screen;

public class BlockParticleIonizer extends BlockMachine{
	public BlockParticleIonizer() {
		super(BlockReference.PARTICLE_IONIZER, false, Screen.PARTICLE_IONIZER);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileParticleIonizer();
	}
}