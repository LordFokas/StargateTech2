package stargatetech2.world;

import net.minecraft.item.ItemStack;
import stargatetech2.IContentModule;
import stargatetech2.StargateTech2;
import stargatetech2.core.util.Color;
import stargatetech2.core.util.Stacks;
import stargatetech2.world.block.BlockLanteanWall;
import stargatetech2.world.worldgen.AncientWorldGenerator;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModuleWorld implements IContentModule {
	public static BlockLanteanWall lanteanWall;
	
	@Override
	public void preInit(){
		lanteanWall = new BlockLanteanWall();
	}

	@Override
	public void init(){
		lanteanWall.registerBlock();
	}

	@Override
	public void postInit(){
		GameRegistry.addShapedRecipe(new ItemStack(lanteanWall, 8, Color.LIGHT_GRAY.id), "SSS", "SNS", "SSS", 'S', Stacks.stone, 'N', Stacks.naqIngot);
		
		for(Color color : Color.COLORS){
			for(int i = 0; i < 16; i++){
				if(i != color.id){
					GameRegistry.addShapelessRecipe(new ItemStack(lanteanWall, 1, color.id), new ItemStack(lanteanWall, 1, i), color.getDye());
				}
			}
		}
		
		StargateTech2.proxy.registerRenderers(Module.WORLD);
		GameRegistry.registerWorldGenerator(new AncientWorldGenerator());
	}

	@Override public void onServerStart(){}
	@Override public void onServerStop(){}

	@Override
	public String getModuleName(){
		return "World";
	}
}