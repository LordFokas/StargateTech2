package stargatetech2.core;

import net.minecraftforge.fluids.FluidRegistry;
import stargatetech2.IContentModule;
import stargatetech2.common.reference.TileEntityReference;
import stargatetech2.core.block.BlockParticleIonizer;
import stargatetech2.core.block.BlockShieldEmitter;
import stargatetech2.core.item.ItemTabletPC;
import stargatetech2.core.tileentity.TileParticleIonizer;
import stargatetech2.core.tileentity.TileShieldEmitter;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public final class ModuleCore implements IContentModule{
	public BlockShieldEmitter shieldEmitter;
	public BlockParticleIonizer particleIonizer;
	
	public ItemTabletPC tabletPC;
	
	@Override
	public void preInit(){
		shieldEmitter = new BlockShieldEmitter();
		particleIonizer = new BlockParticleIonizer();
		
		tabletPC = new ItemTabletPC();
	}

	@Override
	public void init(){
		FluidRegistry.registerFluid(IonizedParticles.fluid);
		
		shieldEmitter.registerBlock();
		particleIonizer.registerBlock();
		
		LanguageRegistry.addName(shieldEmitter, "Shield Emitter");
		LanguageRegistry.addName(particleIonizer, "Particle Ionizer");
		LanguageRegistry.addName(tabletPC, "Tablet PC");
		
		GameRegistry.registerTileEntity(TileShieldEmitter.class, TileEntityReference.TILE_SHIELD_EMITTER);
		GameRegistry.registerTileEntity(TileParticleIonizer.class, TileEntityReference.TILE_PARTICLE_IONIZER);
	}

	@Override
	public void postInit(){}

	@Override
	public void onServerStart(){}

	@Override
	public void onServerStop(){}

	@Override
	public String getModuleName() {
		return "Core";
	}
}