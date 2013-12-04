package stargatetech2.core.rendering;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import stargatetech2.common.base.BaseISBRH;

public class RenderStargateBlock extends BaseISBRH {
	private static final RenderStargateBlock INSTANCE = new RenderStargateBlock();
	
	public static RenderStargateBlock instance(){
		return INSTANCE;
	}
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		return true;
	}
	
	@Override
	public boolean shouldRender3DInInventory(){
		return false;
	}
}
