package stargatetech2.integration.plugins.cc;

import stargatetech2.common.reference.ConfigReference;
import stargatetech2.common.reference.TileEntityReference;
import stargatetech2.integration.plugins.BasePlugin;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class PluginCC extends BasePlugin {
	public static BlockBusAdapter busAdapter;
	
	public PluginCC() {
		super("ComputerCraft", ConfigReference.KEY_PLUGINS_CC);
	}

	@Override
	public void load() {
		busAdapter = new BlockBusAdapter();
		busAdapter.registerBlock();
		GameRegistry.registerTileEntity(TileBusAdapter.class, TileEntityReference.TILE_BUS_ADAPTER);
		LanguageRegistry.addName(busAdapter, "Abstract Bus Adapter");
	}
}