package lordfokas.stargatetech2;

import lordfokas.stargatetech2.modules.IContentModule.Module;
import lordfokas.stargatetech2.util.GUIHandler;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class ProxyServer implements ISidedProxy{
	
	/* Unimplemented Client-Side methods. */
	@Override public void registerRenderers(Module module){}
	@Override public boolean isLocalPlayerInAABB(World world, AxisAlignedBB aabb){ return false; }

	@Override
	public void registerHandlers() {
		NetworkRegistry.INSTANCE.registerGuiHandler(StargateTech2.instance, new GUIHandler());
	}
}