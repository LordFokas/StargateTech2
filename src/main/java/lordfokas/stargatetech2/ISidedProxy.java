package lordfokas.stargatetech2;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import lordfokas.stargatetech2.IContentModule.Module;

public interface ISidedProxy {
	public void registerRenderers(Module module);
	public void registerHandlers();
	public boolean isLocalPlayerInAABB(World world, AxisAlignedBB aabb);
	public void registerLanguages();
}
