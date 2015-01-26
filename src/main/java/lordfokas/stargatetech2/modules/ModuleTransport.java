package lordfokas.stargatetech2.modules;

import lordfokas.stargatetech2.StargateTech2;
import lordfokas.stargatetech2.modules.transport.BlockBeacon;
import lordfokas.stargatetech2.modules.transport.BlockInvisible;
import lordfokas.stargatetech2.modules.transport.BlockNaquadahRail;
import lordfokas.stargatetech2.modules.transport.BlockStargate;
import lordfokas.stargatetech2.modules.transport.BlockTransportRing;
import lordfokas.stargatetech2.modules.transport.TileBeaconConsole;
import lordfokas.stargatetech2.modules.transport.TileBeaconMatterGrid;
import lordfokas.stargatetech2.modules.transport.TileBeaconTransceiver;
import lordfokas.stargatetech2.modules.transport.TileStargate;
import lordfokas.stargatetech2.modules.transport.TileStargateBase;
import lordfokas.stargatetech2.modules.transport.TileStargateRing;
import lordfokas.stargatetech2.modules.transport.TileTransportRing;
import lordfokas.stargatetech2.modules.transport.stargates.StargateNetwork;
import lordfokas.stargatetech2.reference.TileEntityReference;
import lordfokas.stargatetech2.util.Stacks;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModuleTransport implements IContentModule {
	public static BlockBeacon beacon;
	public static BlockNaquadahRail naquadahRail;
	public static BlockTransportRing transportRing;
	public static BlockInvisible invisible;
	public static BlockStargate stargate;
	
	@Override
	public void preInit(){
		beacon = new BlockBeacon();
		naquadahRail = new BlockNaquadahRail();
		transportRing = new BlockTransportRing();
		invisible = new BlockInvisible();
		stargate = new BlockStargate();
		
		naquadahRail.registerBlock();
	}

	@Override
	public void init(){
		GameRegistry.registerTileEntity(TileBeaconConsole.class, TileEntityReference.TILE_BEACON_CONSOLE);
		GameRegistry.registerTileEntity(TileBeaconMatterGrid.class, TileEntityReference.TILE_BEACON_MATTERGRID);
		GameRegistry.registerTileEntity(TileBeaconTransceiver.class, TileEntityReference.TILE_BEACON_TRANSCEIVER);
		GameRegistry.registerTileEntity(TileTransportRing.class, TileEntityReference.TILE_TRANSPORT_RING);
		GameRegistry.registerTileEntity(TileStargate.class, TileEntityReference.TILE_STARGATE);
		GameRegistry.registerTileEntity(TileStargateRing.class, TileEntityReference.TILE_STARGATE_RING);
		GameRegistry.registerTileEntity(TileStargateBase.class, TileEntityReference.TILE_STARGATE_BASE);
	}

	@Override
	public void postInit(){	
		GameRegistry.addShapedRecipe(new ItemStack(naquadahRail, 2), "N-N", "NSN", "N-N", 'N', Stacks.naqIngot, 'S', Stacks.stick);
		GameRegistry.addShapedRecipe(new ItemStack(transportRing), "-S-", "PMP", "NCN", 'S', Stacks.slab, 'N', Stacks.naqIngot, 'P', Stacks.naqPlate, 'C', Stacks.coilEnd, 'M', Stacks.machine_0);
		
		GameRegistry.addShapedRecipe(new ItemStack(beacon, 1, BlockBeacon.META_TRANSCEIVER), "ISI", "SMS", "ICI", 'I', Stacks.coilNaq, 'C', Stacks.circuit, 'M', Stacks.machine_2, 'S', Stacks.signalum);
		GameRegistry.addShapedRecipe(new ItemStack(beacon, 1, BlockBeacon.META_ANTENNA), "-N-", "NSN", "-N-", 'N', Stacks.naqIngot, 'S', Stacks.signalum);
		GameRegistry.addShapedRecipe(new ItemStack(beacon, 1, BlockBeacon.META_CONSOLE), "-C-", "GMG", "NPN", 'N', Stacks.naqIngot, 'C', Stacks.circuit, 'G', Stacks.glass, 'M', Stacks.machine_0, 'P', Stacks.coilGold);
		GameRegistry.addShapedRecipe(new ItemStack(beacon, 4, BlockBeacon.META_MATTERGRID), "ECE", "CMC", "ECE", 'E', Stacks.coilEnd, 'C', Stacks.circuit, 'M', Stacks.machine_1);
		
		StargateTech2.proxy.registerRenderers(Module.TRANSPORT);
	}

	@Override public void onServerStart(){
		StargateNetwork.instance().load();
		// TODO: This has issues, fix 'em.
		// ((ServerCommandManager) MinecraftServer.getServer().getCommandManager()).registerCommand(new CommandGateTP());
	}
	
	@Override public void onServerStop(){
		StargateNetwork.instance().unload();
	}

	@Override
	public String getModuleName(){
		return "Transport";
	}
}