package stargatetech2.core;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.MinecraftForge;
import stargatetech2.IContentModule;
import stargatetech2.StargateTech2;
import stargatetech2.core.api.StackManager;
import stargatetech2.core.block.BlockNaquadahOre;
import stargatetech2.core.item.ItemNaquadah;
import stargatetech2.core.item.ItemNaquadah.Metadata;
import stargatetech2.core.item.ItemTabletPC;
import stargatetech2.core.util.ChunkLoader;
import stargatetech2.core.util.Color;
import stargatetech2.core.util.CoreEventHandler;
import stargatetech2.core.util.CoreWorldGenerator;
import stargatetech2.core.util.Stacks;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public final class ModuleCore implements IContentModule{
	public static BlockNaquadahOre naquadahOre;
	
	public static ItemTabletPC tabletPC;
	public static ItemNaquadah naquadah;
	
	@Override
	public void preInit(){
		naquadahOre = new BlockNaquadahOre();
		tabletPC = new ItemTabletPC();
		naquadah = new ItemNaquadah();
	}

	@Override
	public void init(){
		naquadahOre.registerBlock();
		
		StackManager manager = StackManager.instance;
		manager.addStack("naquadahOre", new ItemStack(naquadahOre));
		manager.addStack("tabletPC", new ItemStack(tabletPC));
		for(Metadata meta : naquadah.DATA){
			manager.addStack(meta.iconName, new ItemStack(naquadah, 1, meta.ID));
		}
	}

	@Override
	public void postInit(){
		MinecraftForge.EVENT_BUS.register(new CoreEventHandler());
		
		LanguageRegistry.addName(naquadahOre, "Naquadah Ore");
		LanguageRegistry.addName(tabletPC, "Tablet PC");
		String[] names = naquadah.getItemNames();
		for(int i = 0; i < names.length; i++){
			LanguageRegistry.addName(new ItemStack(naquadah, 1, i), names[i]);
		}
		
		StargateTech2.proxy.registerRenderers(Module.CORE);
		GameRegistry.registerWorldGenerator(new CoreWorldGenerator());
		ChunkLoader.register();
		Stacks.init();
		
		GameRegistry.addSmelting(naquadahOre.blockID, Stacks.naqIngot, 0);
		FurnaceRecipes.smelting().addSmelting(naquadah.itemID, ItemNaquadah.LATTICE.ID, Stacks.circuit, 0);
		FurnaceRecipes.smelting().addSmelting(naquadah.itemID, ItemNaquadah.DUST.ID, Stacks.naqIngot, 0);
		
		GameRegistry.addShapedRecipe(new ItemStack(tabletPC), "NNN", "RGR", "NNN", 'N', Stacks.naqIngot, 'R', Stacks.redstone, 'G', Stacks.glass);
		
		GameRegistry.addShapelessRecipe(new ItemStack(naquadah, 3, ItemNaquadah.LATTICE.ID), Stacks.quartz, Stacks.quartz, Stacks.naqDust);
		GameRegistry.addShapedRecipe(new ItemStack(naquadah, 4, ItemNaquadah.BAR.ID), "--S", "-S-", "S--", 'S', Stacks.naqIngot);
		GameRegistry.addShapedRecipe(new ItemStack(naquadah, 2, ItemNaquadah.PLATE.ID), "SS", "SS", 'S', Stacks.naqIngot);
		
		GameRegistry.addShapedRecipe(Stacks.crystal1, "GNG", "NNN", "CNC", 'N', Stacks.naqIngot, 'C', Stacks.circuit, 'G', Color.GREEN.getDye());
		GameRegistry.addShapedRecipe(Stacks.crystal2, "YNY", "NGN", "CDC", 'N', Stacks.naqIngot, 'C', Stacks.circuit, 'G', Stacks.crystal1, 'D', Stacks.diamond, 'Y', Color.YELLOW.getDye());
		GameRegistry.addShapedRecipe(Stacks.crystal3, "RNR", "NYN", "CDC", 'N', Stacks.naqIngot, 'C', Stacks.circuit, 'Y', Stacks.crystal2, 'D', Stacks.diamond, 'R', Color.RED.getDye());
	}

	@Override public void onServerStart(){
		ChunkLoader.load();
	}
	
	@Override public void onServerStop(){
		ChunkLoader.unload();
	}

	@Override
	public String getModuleName() {
		return "Core";
	}
}