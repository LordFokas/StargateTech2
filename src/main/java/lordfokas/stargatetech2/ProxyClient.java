package lordfokas.stargatetech2;

import java.util.List;

import lordfokas.stargatetech2.IContentModule.Module;
import lordfokas.stargatetech2.automation.rendering.RenderBusCable;
import lordfokas.stargatetech2.core.base.BaseISBRH;
import lordfokas.stargatetech2.core.machine.RenderBlockMachine;
import lordfokas.stargatetech2.core.rendering.RenderNaquadah;
import lordfokas.stargatetech2.core.util.GUIHandlerClient;
import lordfokas.stargatetech2.transport.rendering.RenderNaquadahRail;
import lordfokas.stargatetech2.transport.rendering.RenderStargateBlock;
import lordfokas.stargatetech2.transport.rendering.RenderStargateTile;
import lordfokas.stargatetech2.transport.rendering.RenderTransportRing;
import lordfokas.stargatetech2.transport.tileentity.TileStargate;
import lordfokas.stargatetech2.transport.tileentity.TileTransportRing;
import lordfokas.stargatetech2.transport.util.RingKeyHandler;
import lordfokas.stargatetech2.world.rendering.RenderLanteanWall;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
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
				registerRenderer(RenderBlockMachine.instance());
				registerRenderer(RenderNaquadah.instance());
				break;
			case ENEMY:
				break;
			case ENERGY:
				break;
			case FACTORY:
				break;
			case INTEGRATION:
				break;
			case TRANSPORT:
				registerRenderer(RenderNaquadahRail.instance());
				registerRenderer(RenderStargateBlock.instance());
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
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GUIHandlerClient());
		RingKeyHandler.register();
	}
	
	private void registerRenderer(BaseISBRH renderer){
		RenderingRegistry.registerBlockHandler(renderer);
	}

	@Override
	public boolean isLocalPlayerInAABB(World world, AxisAlignedBB aabb){
		List<Entity> players = world.getEntitiesWithinAABB(EntityClientPlayerMP.class, aabb);
		return players.size() == 1;
	}

	@Override
	public void registerLanguages() {
		LanguageRegistry.instance().loadLocalization("/assets/stargatetech2/lang/en_US.lang", "en_US", false);
	}
}