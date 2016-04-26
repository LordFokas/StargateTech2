package lordfokas.stargatetech2;

import java.util.List;

import lordfokas.stargatetech2.modules.IContentModule.Module;
import lordfokas.stargatetech2.modules.transport.RenderBeaconMatterGrid;
import lordfokas.stargatetech2.modules.transport.RenderStargateTile;
import lordfokas.stargatetech2.modules.transport.RenderTransportRing;
import lordfokas.stargatetech2.modules.transport.RingKeyHandler;
import lordfokas.stargatetech2.modules.transport.RingOverlay;
import lordfokas.stargatetech2.modules.transport.TileBeaconMatterGrid;
import lordfokas.stargatetech2.modules.transport.TileStargate;
import lordfokas.stargatetech2.modules.transport.TileTransportRing;
import lordfokas.stargatetech2.util.GUIHandlerClient;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ProxyClient implements ISidedProxy{

	@Override
	public void registerRenderers(Module module){
		switch(module){
			case AUTOMATION:
				break;
			case CORE:
				break;
			case ENEMY:
				break;
			case ENERGY:
				break;
			case INTEGRATION:
				break;
			case TRANSPORT: // FIXME whatever is wrong with these generics
				//ClientRegistry.bindTileEntitySpecialRenderer(TileBeaconMatterGrid.class, new RenderBeaconMatterGrid());
				//ClientRegistry.bindTileEntitySpecialRenderer(TileTransportRing.class, new RenderTransportRing());
				//ClientRegistry.bindTileEntitySpecialRenderer(TileStargate.class, new RenderStargateTile());
				break;
			case WORLD:
				break;
		}
	}
	
	@Override
	public void registerHandlers() {
		NetworkRegistry.INSTANCE.registerGuiHandler(StargateTech2.instance, new GUIHandlerClient());
		RingKeyHandler.register();
		RingOverlay.register();
	}

	@Override
	public boolean isLocalPlayerInAABB(World world, AxisAlignedBB aabb){ // FIXME might be wrong class
		List<EntityPlayerMP> players = world.getEntitiesWithinAABB(EntityPlayerMP.class, aabb);
		return players.size() == 1;
	}
}