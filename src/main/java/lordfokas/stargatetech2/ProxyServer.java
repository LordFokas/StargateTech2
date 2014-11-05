package lordfokas.stargatetech2;

import lordfokas.stargatetech2.IContentModule.Module;
import lordfokas.stargatetech2.core.util.GUIHandler;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.NetworkRegistry;

public class ProxyServer implements ISidedProxy{
	
	/* Unimplemented Client-Side methods. */
	@Override public void registerRenderers(Module module){}
	@Override public boolean isLocalPlayerInAABB(World world, AxisAlignedBB aabb){ return false; }

	@Override
	public void registerHandlers() {
		NetworkRegistry.INSTANCE.registerGuiHandler(StargateTech2.instance, new GUIHandler());
	}
}