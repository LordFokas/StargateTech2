package lordfokas.stargatetech2.modules.transport;

import lordfokas.stargatetech2.ZZ_THRASH.BaseISBRH_THRASH;
import lordfokas.stargatetech2.modules.ModuleEnemy;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

public class RenderNaquadahRail extends BaseISBRH_THRASH {
	private static final RenderNaquadahRail INSTANCE = new RenderNaquadahRail();
	
	public static RenderNaquadahRail instance(){
		return INSTANCE;
	}
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		if(BlockNaquadahRail.currentRenderPass == 0){
			BlockNaquadahRail rail = (BlockNaquadahRail) block;
			rail.toggleRenderOverride();
			renderer.renderBlockByRenderType(rail, x, y, z);
			rail.toggleRenderOverride();
		}else{
			if((world.getBlockMetadata(x, y, z) & 8) != 0){
				renderer.setRenderBoundsFromBlock(ModuleEnemy.shield);
				renderer.renderStandardBlock(ModuleEnemy.shield, x, y, z);
			}
		}
		return true;
	}
	
	@Override
	public boolean shouldRender3DInInventory(int modelId){
		return false;
	}
}