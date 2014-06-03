package stargatetech2.factory;

import stargatetech2.IContentModule;
import stargatetech2.StargateTech2;
import stargatetech2.core.reference.TileEntityReference;
import stargatetech2.factory.block.BlockCrossover;
import stargatetech2.factory.block.BlockPrioritizer;
import stargatetech2.factory.tileentity.TileCrossover;
import stargatetech2.factory.tileentity.TilePrioritizer;
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
		// GameRegistry.registerTileEntity(TilePrioritizer.class, TileEntityReference.TILE_PRIORITIZER);
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