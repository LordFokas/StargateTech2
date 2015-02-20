package lordfokas.stargatetech2;

import java.util.List;

import lordfokas.stargatetech2.ZZ_THRASH.RenderBlockMachine__THRASH;
import lordfokas.stargatetech2.lib.render.BaseISBRH;
import lordfokas.stargatetech2.modules.IContentModule.Module;
import lordfokas.stargatetech2.modules.automation.RenderBusCable;
import lordfokas.stargatetech2.modules.core.RenderNaquadah;
import lordfokas.stargatetech2.modules.transport.RenderBeacon;
import lordfokas.stargatetech2.modules.transport.RenderBeaconMatterGrid;
import lordfokas.stargatetech2.modules.transport.RenderNaquadahRail;
import lordfokas.stargatetech2.modules.transport.RenderStargateBlock;
import lordfokas.stargatetech2.modules.transport.RenderStargateTile;
import lordfokas.stargatetech2.modules.transport.RenderTransportRing;
import lordfokas.stargatetech2.modules.transport.RingKeyHandler;
import lordfokas.stargatetech2.modules.transport.RingOverlay;
import lordfokas.stargatetech2.modules.transport.TileBeaconMatterGrid;
import lordfokas.stargatetech2.modules.transport.TileStargate;
import lordfokas.stargatetech2.modules.transport.TileTransportRing;
import lordfokas.stargatetech2.modules.world.RenderLanteanWall;
import lordfokas.stargatetech2.util.GUIHandlerClient;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ProxyClient implements ISidedProxy{

	@Override
	public void registerRenderers(Module module){
		switch(module){
			case AUTOMATION:
				registerRenderer(RenderBusCable.instance());
				break;
			case CORE:
				registerRenderer(RenderBlockMachine__THRASH.instance());
				registerRenderer(RenderNaquadah.instance());
				break;
			case ENEMY:
				break;
			case ENERGY:
				break;
			case INTEGRATION:
				break;
			case TRANSPORT:
				registerRenderer(RenderBeacon.instance());
				registerRenderer(RenderNaquadahRail.instance());
				registerRenderer(RenderStargateBlock.instance());
				ClientRegistry.bindTileEntitySpecialRenderer(TileBeaconMatterGrid.class, new RenderBeaconMatterGrid());
				ClientRegistry.bindTileEntitySpecialRenderer(TileTransportRing.class, new RenderTransportRing());
				ClientRegistry.bindTileEntitySpecialRenderer(TileStargate.class, new RenderStargateTile());
				break;
			case WORLD:
				registerRenderer(RenderLanteanWall.instance());
				break;
		}
	}
	
	@Override
	public void registerHandlers() {
		NetworkRegistry.INSTANCE.registerGuiHandler(StargateTech2.instance, new GUIHandlerClient());
		RingKeyHandler.register();
		RingOverlay.register();
	}
	
	private void registerRenderer(BaseISBRH renderer){
		RenderingRegistry.registerBlockHandler(renderer);
	}

	@Override
	public boolean isLocalPlayerInAABB(World world, AxisAlignedBB aabb){
		List<Entity> players = world.getEntitiesWithinAABB(EntityClientPlayerMP.class, aabb);
		return players.size() == 1;
	}
}