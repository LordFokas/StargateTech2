package stargatetech2.integration.plugins.cc;

import net.minecraft.item.ItemStack;
import stargatetech2.core.reference.ConfigReference;
import stargatetech2.core.reference.TileEntityReference;
import stargatetech2.core.util.Stacks;
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
		ItemStack plate, cable, circuit;
		GameRegistry.addShapedRecipe(new ItemStack(busAdapter), "-C-", "PMP", "NBN", 'P', Stacks.naqPlate, 'B', Stacks.busCable, 'C', Stacks.circuit, 'N', Stacks.naqIngot, 'M', Stacks.machine);
	}

	@Override protected void fallback(){}
}