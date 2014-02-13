package stargatetech2.factory;

import stargatetech2.IContentModule;
import stargatetech2.StargateTech2;
import stargatetech2.core.reference.TileEntityReference;
import stargatetech2.factory.block.BlockCrossover;
import stargatetech2.factory.tileentity.TileEntityCrossover;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModuleFactory implements IContentModule {
	public static BlockCrossover crossover;
	
	@Override
	public void preInit(){
		crossover = new BlockCrossover();
	}

	@Override
	public void init(){
		crossover.registerBlock();
		
		GameRegistry.registerTileEntity(TileEntityCrossover.class, TileEntityReference.TILE_CROSSOVER);
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