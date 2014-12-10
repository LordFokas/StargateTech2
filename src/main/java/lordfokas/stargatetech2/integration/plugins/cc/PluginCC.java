package lordfokas.stargatetech2.integration.plugins.cc;

import lordfokas.stargatetech2.core.reference.TileEntityReference;
import lordfokas.stargatetech2.core.util.Stacks;
import lordfokas.stargatetech2.integration.plugins.IPlugin;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;
import dan200.computercraft.api.ComputerCraftAPI;

public class PluginCC implements IPlugin{
	public static BlockBusAdapter busAdapter;
	
	@Override
	public void load() {
		busAdapter = new BlockBusAdapter();
		GameRegistry.registerTileEntity(TileBusAdapter.class, TileEntityReference.TILE_BUS_ADAPTER);
		ItemStack plate, cable, circuit;
		GameRegistry.addShapedRecipe(new ItemStack(busAdapter), "-C-", "PMP", "NBN", 'P', Stacks.naqPlate, 'B', Stacks.busCable, 'C', Stacks.circuit, 'N', Stacks.naqIngot, 'M', Stacks.machine_0);
		ComputerCraftAPI.registerPeripheralProvider(new CCPeripheralProvider());
	}
	
	@Override public void postload(){}
}
