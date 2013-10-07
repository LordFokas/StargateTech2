package stargatetech2.core.rendering;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import stargatetech2.common.base.BaseISBRH;
import stargatetech2.common.util.Color;
import stargatetech2.core.ModuleCore;

public class RenderLanteanWall extends BaseISBRH {
	private static final RenderLanteanWall INSTANCE = new RenderLanteanWall();
	
	public static RenderLanteanWall instance(){
		return INSTANCE;
	}
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		Color color = getRenderColor(world.getBlockMetadata(x, y, z));
		renderer.renderStandardBlockWithColorMultiplier(block, x, y, z, color.r(), color.g(), color.b());
		return true;
	}
	
	@Override
	protected Color getRenderColor(int metadata){
		return ModuleCore.lanteanWall.getColor(metadata);
	}
}
