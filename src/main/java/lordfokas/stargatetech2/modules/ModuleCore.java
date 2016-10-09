package lordfokas.stargatetech2.modules;

import lordfokas.naquadria.item.ItemEnum;
import lordfokas.stargatetech2.StargateTech2;
import lordfokas.stargatetech2.modules.core.*;
import lordfokas.stargatetech2.reference.ItemReference;
import lordfokas.stargatetech2.util.ChunkLoader;
import lordfokas.stargatetech2.util.Stacks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

public final class ModuleCore implements IContentModule{
	public static BlockNaquadah naquadahBlock;
	
	public static ItemTabletPC tabletPC;
	public static ItemEnum<NaquadahItems> naquadahItem;
	
	@Override
	public void preInit(){
		naquadahBlock = new BlockNaquadah();
		
		naquadahItem = new ItemEnum(NaquadahItems.class, ItemReference.NAQUADAH);
		tabletPC = new ItemTabletPC();
		
		StargateTech2.register(naquadahBlock);
		StargateTech2.register(new ItemBlockNaquadah(naquadahBlock));
		
        StargateTech2.register(naquadahItem);
        StargateTech2.register(tabletPC);
	}

	@Override
	public void init(){
		MinecraftForge.EVENT_BUS.register(new CoreEventHandler());
		GameRegistry.registerWorldGenerator(new CoreWorldGenerator(), 0);
	}

	@Override
	public void postInit(){
		StargateTech2.proxy.registerRenderers(Module.CORE);
		
		FurnaceRecipes.instance().addSmeltingRecipe(Stacks.naqOre, Stacks.naqIngot, 0);
		FurnaceRecipes.instance().addSmeltingRecipe(naquadahItem.asStack(NaquadahItems.NAQUADAH_DUST, 1), Stacks.naqIngot, 0);
		GameRegistry.addShapedRecipe(new ItemStack(tabletPC), "NNN", "CGC", "NRN", 'N', Stacks.naqPlate, 'C', Stacks.circuit, 'G', Stacks.glass, 'R', Stacks.redDust);
		GameRegistry.addShapedRecipe(naquadahItem.asStack(NaquadahItems.NAQUADAH_PLATE, 2), "SS", "SS", 'S', Stacks.naqIngot);
		GameRegistry.addShapedRecipe(naquadahItem.asStack(NaquadahItems.COIL_NAQUADAH, 1), "--R", "-N-", "R--", 'R', Stacks.redDust, 'N', Stacks.naqIngot);
		GameRegistry.addShapedRecipe(naquadahItem.asStack(NaquadahItems.COIL_ENDER, 1), "--R", "-E-", "R--", 'R', Stacks.redDust, 'E', Stacks.pearl);
		GameRegistry.addShapedRecipe(Stacks.naqBlock, "NNN", "NNN", "NNN", 'N', Stacks.naqIngot);
		GameRegistry.addShapelessRecipe(naquadahItem.asStack(NaquadahItems.NAQUADAH_INGOT, 9), Stacks.naqBlock);
	}

	@Override
	public void onServerStart(){
		ChunkLoader.load();
	}
	
	@Override
	public void onServerStop(){
		ChunkLoader.unload();
	}

	@Override
	public String getModuleName() {
		return "Core";
	}
}