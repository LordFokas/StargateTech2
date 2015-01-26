package lordfokas.stargatetech2.modules.integration.tico;

import lordfokas.stargatetech2.modules.ModuleCore;
import lordfokas.stargatetech2.modules.core.BlockNaquadah;
import lordfokas.stargatetech2.modules.core.ItemNaquadah;
import lordfokas.stargatetech2.modules.integration.IPlugin;
import lordfokas.stargatetech2.util.Stacks;
import lordfokas.stargatetech2.util.StargateLogger;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.crafting.LiquidCasting;
import tconstruct.library.crafting.Smeltery;

public class PluginTiCo implements IPlugin{
	private static final int NUGGET = 16; // mB
	private static final int INGOT = NUGGET * 9; // 144 mB
	private static final int BLOCK = INGOT * 9; // 1296 mB
	
	@Override public void load(){}
	
	@Override
	public void postload(){
		ItemStack ingotCast = TConstructRegistry.getItemStack("ingotCast");
		ItemStack plateCast = TConstructRegistry.getItemStack("largePlateCast");
		if(ingotCast == null || plateCast == null){
			StargateLogger.warning("Error finding Tinker's Construct casts, aborting integration!");
			return;
		}
		
		FluidRegistry.registerFluid(MoltenNaquadah.instance);
		
		Smeltery.addMelting(new ItemStack(ModuleCore.naquadahBlock), 800, new FluidStack(MoltenNaquadah.instance, INGOT * 2));
		Smeltery.addMelting(Stacks.naqIngot, ModuleCore.naquadahBlock, BlockNaquadah.BLOCK, 450, new FluidStack(MoltenNaquadah.instance, INGOT * 1));
		Smeltery.addMelting(Stacks.naqDust, ModuleCore.naquadahBlock, BlockNaquadah.BLOCK, 350, new FluidStack(MoltenNaquadah.instance, INGOT * 1));
		Smeltery.addMelting(Stacks.naqPlate, ModuleCore.naquadahBlock, BlockNaquadah.BLOCK, 800, new FluidStack(MoltenNaquadah.instance, INGOT * 2));
		Smeltery.addMelting(Stacks.naqBlock, ModuleCore.naquadahBlock, BlockNaquadah.BLOCK, 1000, new FluidStack(MoltenNaquadah.instance, BLOCK));
		
		LiquidCasting tableCasting = TConstructRegistry.getTableCasting();
		tableCasting.addCastingRecipe(Stacks.naqIngot, new FluidStack(MoltenNaquadah.instance, INGOT * 1), ingotCast, 50);
		tableCasting.addCastingRecipe(new ItemStack(ModuleCore.naquadahItem, 4, ItemNaquadah.PLATE.ID), new FluidStack(MoltenNaquadah.instance, INGOT * 8), plateCast, 50);
		
		LiquidCasting basinCasting = TConstructRegistry.getBasinCasting();
		basinCasting.addCastingRecipe(Stacks.naqBlock, new FluidStack(MoltenNaquadah.instance, BLOCK), 50);
	}
}
