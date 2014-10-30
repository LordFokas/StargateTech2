package lordfokas.stargatetech2.integration.plugins.tico;

import lordfokas.stargatetech2.core.ModuleCore;
import lordfokas.stargatetech2.core.item.ItemNaquadah;
import lordfokas.stargatetech2.core.reference.ConfigReference;
import lordfokas.stargatetech2.core.util.Stacks;
import lordfokas.stargatetech2.core.util.StargateLogger;
import lordfokas.stargatetech2.integration.plugins.BasePlugin;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.crafting.LiquidCasting;
import tconstruct.library.crafting.Smeltery;

public class PluginTiCo extends BasePlugin {
	private static final int NUGGET = 16; // mB
	private static final int INGOT = NUGGET * 9; // 144 mB
	private static final int BLOCK = INGOT * 9; // 1296 mB
	
	public PluginTiCo(){
		super("TConstruct", ConfigReference.KEY_PLUGINS_TICO);
	}
	
	@Override protected void load(){}
	
	@Override
	protected void postLoad(){
		ItemStack ingotCast = TConstructRegistry.getItemStack("ingotCast");
		ItemStack plateCast = TConstructRegistry.getItemStack("largePlateCast");
		if(ingotCast == null || plateCast == null){
			StargateLogger.warning("Error finding Tinker's Construct casts, aborting integration!");
			return;
		}
		
		FluidRegistry.registerFluid(MoltenNaquadah.instance);
		
		Smeltery.addMelting(new ItemStack(ModuleCore.naquadahBlock), 800, new FluidStack(MoltenNaquadah.instance, INGOT * 2));
		Smeltery.addMelting(Stacks.naqIngot, 450, new FluidStack(MoltenNaquadah.instance, INGOT * 1));
		Smeltery.addMelting(Stacks.naqDust, 350, new FluidStack(MoltenNaquadah.instance, INGOT * 1));
		Smeltery.addMelting(Stacks.naqPlate, 800, new FluidStack(MoltenNaquadah.instance, INGOT * 2));
		Smeltery.addMelting(Stacks.naqBlock, 1000, new FluidStack(MoltenNaquadah.instance, BLOCK));
		
		LiquidCasting tableCasting = TConstructRegistry.getTableCasting();
		tableCasting.addCastingRecipe(Stacks.naqIngot, new FluidStack(MoltenNaquadah.instance, INGOT * 1), ingotCast, 50);
		tableCasting.addCastingRecipe(new ItemStack(ModuleCore.naquadahItem, 4, ItemNaquadah.PLATE.ID), new FluidStack(MoltenNaquadah.instance, INGOT * 8), plateCast, 50);
		
		LiquidCasting basinCasting = TConstructRegistry.getBasinCasting();
		basinCasting.addCastingRecipe(Stacks.naqBlock, new FluidStack(MoltenNaquadah.instance, BLOCK), 50);
	}

	@Override protected void fallback(){}
}
