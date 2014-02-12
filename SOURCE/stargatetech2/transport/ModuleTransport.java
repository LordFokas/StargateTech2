package stargatetech2.transport;

import net.minecraft.item.ItemStack;
import stargatetech2.IContentModule;
import stargatetech2.StargateTech2;
import stargatetech2.core.reference.TileEntityReference;
import stargatetech2.core.util.Stacks;
import stargatetech2.transport.block.BlockInvisible;
import stargatetech2.transport.block.BlockNaquadahRail;
import stargatetech2.transport.block.BlockStargate;
import stargatetech2.transport.block.BlockTransportRing;
import stargatetech2.transport.stargates.StargateNetwork;
import stargatetech2.transport.tileentity.TileStargate;
import stargatetech2.transport.tileentity.TileStargateBase;
import stargatetech2.transport.tileentity.TileStargateRing;
import stargatetech2.transport.tileentity.TileTransportRing;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ModuleTransport implements IContentModule {
	public static BlockNaquadahRail naquadahRail;
	public static BlockTransportRing transportRing;
	public static BlockInvisible invisible;
	public static BlockStargate stargate;
	
	@Override
	public void preInit(){
		naquadahRail = new BlockNaquadahRail();
		transportRing = new BlockTransportRing();
		invisible = new BlockInvisible();
		stargate = new BlockStargate();
	}

	@Override
	public void init(){
		naquadahRail.registerBlock();
		transportRing.registerBlock();
		invisible.registerBlock();
		stargate.registerBlock();
		
		GameRegistry.registerTileEntity(TileTransportRing.class, TileEntityReference.TILE_TRANSPORT_RING);
		GameRegistry.registerTileEntity(TileStargate.class, TileEntityReference.TILE_STARGATE);
		GameRegistry.registerTileEntity(TileStargateRing.class, TileEntityReference.TILE_STARGATE_RING);
		GameRegistry.registerTileEntity(TileStargateBase.class, TileEntityReference.TILE_STARGATE_BASE);
	}

	@Override
	public void postInit(){	
		GameRegistry.addShapedRecipe(new ItemStack(naquadahRail, 2), "N-N", "NSN", "N-N", 'N', Stacks.naqIngot, 'S', Stacks.stick);
		GameRegistry.addShapedRecipe(new ItemStack(transportRing), "-S-", "PMP", "NCN", 'S', Stacks.slab, 'N', Stacks.naqIngot, 'P', Stacks.naqPlate, 'C', Stacks.coilEnd, 'M', Stacks.machine);
		
		StargateTech2.proxy.registerRenderers(Module.TRANSPORT);
	}

	@Override public void onServerStart(){
		StargateNetwork.instance().load();
	}
	
	@Override public void onServerStop(){
		StargateNetwork.instance().unload();
	}

	@Override
	public String getModuleName(){
		return "Transport";
	}
}