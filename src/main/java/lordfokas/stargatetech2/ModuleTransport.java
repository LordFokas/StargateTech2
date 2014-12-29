package lordfokas.stargatetech2;

import lordfokas.stargatetech2.core.Stacks;
import lordfokas.stargatetech2.core.reference.TileEntityReference;
import lordfokas.stargatetech2.transport.BlockBeacon;
import lordfokas.stargatetech2.transport.BlockInvisible;
import lordfokas.stargatetech2.transport.BlockNaquadahRail;
import lordfokas.stargatetech2.transport.BlockStargate;
import lordfokas.stargatetech2.transport.BlockTransportRing;
import lordfokas.stargatetech2.transport.TileBeaconConsole;
import lordfokas.stargatetech2.transport.TileBeaconMatterGrid;
import lordfokas.stargatetech2.transport.TileBeaconTransceiver;
import lordfokas.stargatetech2.transport.TileStargate;
import lordfokas.stargatetech2.transport.TileStargateBase;
import lordfokas.stargatetech2.transport.TileStargateRing;
import lordfokas.stargatetech2.transport.TileTransportRing;
import lordfokas.stargatetech2.transport.stargates.StargateNetwork;
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