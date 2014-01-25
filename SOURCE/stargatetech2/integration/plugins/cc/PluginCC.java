package stargatetech2.integration.plugins.cc;

import net.minecraft.item.ItemStack;
import stargatetech2.common.reference.ConfigReference;
import stargatetech2.common.reference.TileEntityReference;
import stargatetech2.core.ModuleCore;
import stargatetech2.core.item.ItemNaquadah;
import stargatetech2.integration.plugins.BasePlugin;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class PluginCC extends BasePlugin {
	public static BlockBusAdapter busAdapter;
	
	public PluginCC() {
		super("ComputerCraft", ConfigReference.KEY_PLUGINS_CC);
	}

	@Override
	protected void load() {
		busAdapter = new BlockBusAdapter();
		busAdapter.registerBlock();
		GameRegistry.registerTileEntity(TileBusAdapter.class, TileEntityReference.TILE_BUS_ADAPTER);
		LanguageRegistry.addName(busAdapter, "Abstract Bus Adapter");
		ItemStack plate, cable, circuit;
		plate = new ItemStack(ModuleCore.naquadah, 1, ItemNaquadah.PLATE.ID);
		cable = new ItemStack(ModuleCore.busCable);
		circuit = new ItemStack(ModuleCore.naquadah, 1, ItemNaquadah.CIRCUIT.ID);
		GameRegistry.addShapedRecipe(new ItemStack(busAdapter), "PBP", "BCB", "PBP", 'P', plate, 'B', cable, 'C', circuit);
	}

	@Override protected void fallback(){}
}