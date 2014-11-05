package lordfokas.stargatetech2.factory;

import lordfokas.stargatetech2.IContentModule;
import lordfokas.stargatetech2.StargateTech2;
import lordfokas.stargatetech2.core.reference.TileEntityReference;
import lordfokas.stargatetech2.factory.tileentity.TilePrioritizer;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModuleFactory implements IContentModule {
	// public static BlockCrossover crossover;
	// public static BlockPrioritizer prioritizer;
	
	@Override
	public void preInit(){
		// crossover = new BlockCrossover();
		// prioritizer = new BlockPrioritizer();
	}

	@Override
	public void init(){
		// crossover.registerBlock();
		// prioritizer.registerBlock();
		
		// GameRegistry.registerTileEntity(TileCrossover.class, TileEntityReference.TILE_CROSSOVER);
		GameRegistry.registerTileEntity(TilePrioritizer.class, TileEntityReference.TILE_PRIORITIZER);
	}
	
	@Override
	public void postInit(){
		StargateTech2.proxy.registerRenderers(Module.FACTORY);
	}

	@Override public void onServerStart(){}
	@Override public void onServerStop(){}

	@Override
	public String getModuleName(){
		return "Factory";
	}
}