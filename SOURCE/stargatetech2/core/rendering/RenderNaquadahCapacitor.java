package stargatetech2.core.rendering;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import stargatetech2.common.base.BaseISBRH;

public class RenderNaquadahCapacitor extends BaseISBRH {
	private static final RenderNaquadahCapacitor INSTANCE = new RenderNaquadahCapacitor();
	
	public static RenderNaquadahCapacitor instance(){
		return INSTANCE;
	}
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		renderer.renderStandardBlock(block, x, y, z);
		return true;
	}

}
