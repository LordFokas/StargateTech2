package lordfokas.stargatetech2;

import lordfokas.stargatetech2.modules.IContentModule.Module;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public interface ISidedProxy {
	public void registerRenderers(Module module);
	public void registerHandlers();
	public boolean isLocalPlayerInAABB(World world, AxisAlignedBB aabb);
    public void handleBlockModel(Block block, String name);
    public void handleItemModel(Item item, String name);
}
