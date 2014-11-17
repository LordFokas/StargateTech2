package lordfokas.stargatetech2.core;

import lordfokas.stargatetech2.IContentModule;
import lordfokas.stargatetech2.StargateTech2;
import lordfokas.stargatetech2.core.block.BlockNaquadah;
import lordfokas.stargatetech2.core.item.ItemNaquadah;
import lordfokas.stargatetech2.core.item.ItemTabletPC;
import lordfokas.stargatetech2.core.reference.ItemReference;
import lordfokas.stargatetech2.core.util.ChunkLoader;
import lordfokas.stargatetech2.core.util.CoreEventHandler;
import lordfokas.stargatetech2.core.util.CoreWorldGenerator;
import lordfokas.stargatetech2.core.util.Stacks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.registry.GameRegistry;

public final class ModuleCore implements IContentModule{
	public static BlockNaquadah naquadahBlock;
	
	public static ItemTabletPC tabletPC;
	public static ItemNaquadah naquadahItem;
	
	@Override
	public void preInit(){
		naquadahBlock = new BlockNaquadah();
		naquadahItem = new ItemNaquadah();
		tabletPC = new ItemTabletPC();
	}

	@Override
	public void init(){
		MinecraftForge.EVENT_BUS.register(new CoreEventHandler());
		GameRegistry.registerItem(naquadahItem, ItemReference.NAQUADAH);
		GameRegistry.registerItem(tabletPC, ItemReference.TABLET_PC);
		GameRegistry.registerWorldGenerator(new CoreWorldGenerator(), 0);
		Stacks.init();
		ChunkLoader.register();
	}

	@Override
	public void postInit(){
		StargateTech2.proxy.registerRenderers(Module.CORE);
		
		FurnaceRecipes.smelting().func_151394_a(new ItemStack(naquadahBlock, BlockNaquadah.ORE), Stacks.naqIngot, 0);
		FurnaceRecipes.smelting().func_151394_a(new ItemStack(naquadahItem, 1, ItemNaquadah.DUST.ID), Stacks.naqIngot, 0);
		GameRegistry.addShapedRecipe(new ItemStack(tabletPC), "NNN", "CGC", "NRN", 'N', Stacks.naqPlate, 'C', Stacks.circuit, 'G', Stacks.glass, 'R', Stacks.redDust);
		GameRegistry.addShapedRecipe(new ItemStack(naquadahItem, 2, ItemNaquadah.PLATE.ID), "SS", "SS", 'S', Stacks.naqIngot);
		GameRegistry.addShapedRecipe(new ItemStack(naquadahItem, 1, ItemNaquadah.COIL_NAQ.ID), "--R", "-N-", "R--", 'R', Stacks.redDust, 'N', Stacks.naqIngot);
		GameRegistry.addShapedRecipe(new ItemStack(naquadahItem, 1, ItemNaquadah.COIL_END.ID), "--R", "-E-", "R--", 'R', Stacks.redDust, 'E', Stacks.pearl);
		GameRegistry.addShapedRecipe(Stacks.naqBlock, "NNN", "NNN", "NNN", 'N', Stacks.naqIngot);
		GameRegistry.addShapelessRecipe(new ItemStack(naquadahItem, 9, ItemNaquadah.INGOT.ID), Stacks.naqBlock);
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