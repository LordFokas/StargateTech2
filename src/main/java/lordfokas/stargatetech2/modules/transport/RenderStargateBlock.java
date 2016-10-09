package lordfokas.stargatetech2.modules.transport;

import lordfokas.stargatetech2.ZZ_THRASH.BaseISBRH_THRASH;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

public class RenderStargateBlock extends BaseISBRH_THRASH {
	private static final RenderStargateBlock INSTANCE = new RenderStargateBlock();
	
	public static RenderStargateBlock instance(){
		return INSTANCE;
	}
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		BlockStargate sg = (BlockStargate) block;
		if(world.getBlockMetadata(x, y, z) != BlockStargate.META_RING){
			sg.setBaseOverride();
			renderer.renderStandardBlock(sg, x, y, z);
			sg.restoreTextures();
		}
		return true;
	}
	
	@Override
	public boolean shouldRender3DInInventory(int modelId){
		return false;
	}
}