package stargatetech2.integration.plugins.te3;

import net.minecraft.item.ItemStack;
import cofh.util.ThermalExpansionHelper;
import stargatetech2.common.reference.ConfigReference;
import stargatetech2.core.ModuleCore;
import stargatetech2.core.item.ItemNaquadah;
import stargatetech2.integration.ModuleIntegration;
import stargatetech2.integration.plugins.BasePlugin;

public class PluginTE3 extends BasePlugin {

	public PluginTE3() {
		super("ThermalExpansion", ConfigReference.KEY_PLUGINS_TE3);
	}

	@Override
	public void load() {
		ThermalExpansionHelper.addPulverizerRecipe(80, new ItemStack(ModuleCore.naquadahOre), new ItemStack(ModuleCore.naquadah, 2, ItemNaquadah.DUST.ID));
		ThermalExpansionHelper.addPulverizerRecipe(80, new ItemStack(ModuleCore.naquadah, 1, ItemNaquadah.INGOT.ID), new ItemStack(ModuleCore.naquadah, 1, ItemNaquadah.DUST.ID));
		ModuleIntegration.naqDustRecipeAdded = true;
	}
}