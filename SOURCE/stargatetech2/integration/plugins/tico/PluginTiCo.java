package stargatetech2.integration.plugins.tico;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import stargatetech2.core.ModuleCore;
import stargatetech2.core.reference.ConfigReference;
import stargatetech2.core.util.Stacks;
import stargatetech2.integration.plugins.BasePlugin;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.crafting.LiquidCasting;
import tconstruct.library.crafting.Smeltery;

public class PluginTiCo extends BasePlugin {
	private static final int INGOT = 144; // mB
	
	public PluginTiCo(){
		super("TConstruct", ConfigReference.KEY_PLUGINS_TICO);
	}
	
	@Override protected void load(){}
	
	@Override
	protected void postLoad(){
		ItemStack ingotCast = TConstructRegistry.getItemStack("ingotPattern");
		if(ingotCast == null){
			ingotCast = getFallbackIngotCast();
		}
		
		FluidRegistry.registerFluid(MoltenNaquadah.instance);
		
		Smeltery.addMelting(new ItemStack(ModuleCore.naquadahOre), 800, new FluidStack(MoltenNaquadah.instance, INGOT * 2));
		Smeltery.addMelting(Stacks.naqIngot, 800, new FluidStack(MoltenNaquadah.instance, INGOT * 1));
		Smeltery.addMelting(Stacks.naqDust, 800, new FluidStack(MoltenNaquadah.instance, INGOT * 1));
		Smeltery.addMelting(Stacks.naqPlate, 800, new FluidStack(MoltenNaquadah.instance, INGOT * 2));
		
		LiquidCasting casting = TConstructRegistry.getTableCasting();
		casting.addCastingRecipe(Stacks.naqIngot, new FluidStack(MoltenNaquadah.instance, INGOT * 1), ingotCast, 50);
	}
	
	private ItemStack getFallbackIngotCast(){
		for(Item item : Item.itemsList){
			if(item != null){
				if(item.getClass().getCanonicalName().equals("tconstruct.items.MetalPattern")){
					return new ItemStack(item);
				}
			}
		}
		return null;
	}

	@Override protected void fallback(){}
}
