package lordfokas.stargatetech2;

import lordfokas.stargatetech2.IContentModule.Module;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public interface ISidedProxy {
	public void registerRenderers(Module module);
	public void registerHandlers();
	public boolean isLocalPlayerInAABB(World world, AxisAlignedBB aabb);
	public void registerLanguages();
}
