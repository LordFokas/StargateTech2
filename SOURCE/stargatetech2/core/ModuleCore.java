package stargatetech2.core;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.MinecraftForge;
import stargatetech2.IContentModule;
import stargatetech2.StargateTech2;
import stargatetech2.core.block.BlockNaquadahOre;
import stargatetech2.core.item.ItemNaquadah;
import stargatetech2.core.item.ItemTabletPC;
import stargatetech2.core.reference.TileEntityReference;
import stargatetech2.core.util.ChunkLoader;
import stargatetech2.core.util.Color;
import stargatetech2.core.util.CoreEventHandler;
import stargatetech2.core.util.CoreWorldGenerator;
import stargatetech2.core.util.Stacks;
import stargatetech2.enemy.tileentity.TileParticleIonizer;
import stargatetech2.enemy.tileentity.TileShield;
import stargatetech2.enemy.tileentity.TileShieldEmitter;
import stargatetech2.transport.stargates.StargateNetwork;
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
		GameRegistry.registerTileEntity(TileShieldEmitter.class, TileEntityReference.TILE_SHIELD_EMITTER);
		GameRegistry.registerTileEntity(TileParticleIonizer.class, TileEntityReference.TILE_PARTICLE_IONIZER);
		GameRegistry.registerTileEntity(TileShield.class, TileEntityReference.TILE_SHIELD);
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
		
		/*GameRegistry.addSmelting(naquadahOre.blockID, naqIngot, 0);
		FurnaceRecipes.smelting().addSmelting(naquadah.itemID, ItemNaquadah.LATTICE.ID, circuit, 0);
		FurnaceRecipes.smelting().addSmelting(naquadah.itemID, ItemNaquadah.DUST.ID, naqIngot, 0);
		
		GameRegistry.addShapedRecipe(new ItemStack(tabletPC), "NNN", "RGR", "NNN", 'N', naqIngot, 'R', Stacks.redstone, 'G', Stacks.glass);
		
		GameRegistry.addShapelessRecipe(new ItemStack(naquadah, 3, ItemNaquadah.LATTICE.ID), Stacks.quartz, Stacks.quartz, naqDust);
		GameRegistry.addShapedRecipe(new ItemStack(naquadah, 4, ItemNaquadah.BAR.ID), "--S", "-S-", "S--", 'S', naqIngot);
		GameRegistry.addShapedRecipe(new ItemStack(naquadah, 2, ItemNaquadah.PLATE.ID), "SS", "SS", 'S', naqIngot);
		
		GameRegistry.addShapedRecipe(crystal1, "GNG", "NNN", "CNC", 'N', naqIngot, 'C', circuit, 'G', Color.GREEN.getDye());
		GameRegistry.addShapedRecipe(crystal2, "YNY", "NGN", "CDC", 'N', naqIngot, 'C', circuit, 'G', crystal1, 'D', Stacks.diamond, 'Y', Color.YELLOW.getDye());
		GameRegistry.addShapedRecipe(crystal3, "RNR", "NYN", "CDC", 'N', naqIngot, 'C', circuit, 'Y', crystal2, 'D', Stacks.diamond, 'R', Color.RED.getDye());*/
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