package lordfokas.stargatetech2.modules;

import lordfokas.naquadria.render.Color;
import lordfokas.stargatetech2.StargateTech2;
import lordfokas.stargatetech2.modules.world.AncientWorldGenerator;
import lordfokas.stargatetech2.modules.world.BlockLanteanWall;
import lordfokas.stargatetech2.util.Stacks;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModuleWorld implements IContentModule {
	public static BlockLanteanWall lanteanWall;
	
	@Override
	public void preInit(){
		lanteanWall = new BlockLanteanWall();
	}

	@Override
	public void init(){
		GameRegistry.registerWorldGenerator(new AncientWorldGenerator(), 0);
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
	}

	@Override public void onServerStart(){}
	@Override public void onServerStop(){}

	@Override
	public String getModuleName(){
		return "World";
	}
}