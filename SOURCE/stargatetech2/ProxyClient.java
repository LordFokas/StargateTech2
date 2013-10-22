package stargatetech2;

import java.util.List;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import stargatetech2.IContentModule.Module;
import stargatetech2.common.base.BaseISBRH;
import stargatetech2.common.machine.RenderBlockMachine;
import stargatetech2.common.util.GUIHandlerClient;
import stargatetech2.core.rendering.RenderLanteanWall;
import stargatetech2.core.rendering.RenderNaquadahCapacitor;
import stargatetech2.core.rendering.RenderNaquadahOre;
import stargatetech2.core.rendering.RenderNaquadahRail;
import stargatetech2.core.rendering.RenderTransportRing;
import stargatetech2.core.tileentity.TileTransportRing;
import stargatetech2.core.util.RingKeyHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ProxyClient implements ISidedProxy{

	@Override
	public void registerRenderers(Module module){
		switch(module){
			case CORE:
				registerRenderer(RenderBlockMachine.instance());
				registerRenderer(RenderNaquadahRail.instance());
				registerRenderer(RenderNaquadahOre.instance());
				registerRenderer(RenderLanteanWall.instance());
				registerRenderer(RenderNaquadahCapacitor.instance());
				ClientRegistry.bindTileEntitySpecialRenderer(TileTransportRing.class, new RenderTransportRing());
				break;
			case INTEGRATION:
				break;
		}
	}

	@Override
	public void registerHandlers() {
		NetworkRegistry.instance().registerGuiHandler(this, new GUIHandlerClient());
		KeyBindingRegistry.registerKeyBinding(new RingKeyHandler());
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