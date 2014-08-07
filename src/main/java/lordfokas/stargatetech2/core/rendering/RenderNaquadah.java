package lordfokas.stargatetech2.core.rendering;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import lordfokas.stargatetech2.core.base.BaseISBRH;
import lordfokas.stargatetech2.core.reference.TextureReference;
import lordfokas.stargatetech2.core.util.IconRegistry;

public class RenderNaquadah extends BaseISBRH {
	private static final RenderNaquadah INSTANCE = new RenderNaquadah();
	
	public static RenderNaquadah instance(){
		return INSTANCE;
	}
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer){
		Icon glow;
		if(world.getBlockMetadata(x, y, z) == 0){
			Icon face = IconRegistry.blockIcons.get(TextureReference.FACE_NAQUADAH_ORE);
			glow = IconRegistry.blockIcons.get(TextureReference.GLOW_NAQUADAH_ORE);
			renderer.setOverrideBlockTexture(face);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.clearOverrideBlockTexture();
		}else{
			glow = IconRegistry.blockIcons.get(TextureReference.NAQUADAH_BLOCK);
		}
		Tessellator tessellator = Tessellator.instance;
		tessellator.setColorOpaque_I(0xFFFFFF);
		tessellator.setBrightness(220);
		if(block.shouldSideBeRendered(world, x - 1, y, z, 4) || renderer.renderAllFaces){
			renderer.renderFaceXNeg(block, x, y, z, glow);
		}
		if(block.shouldSideBeRendered(world, x, y - 1, z, 0) || renderer.renderAllFaces){
			renderer.renderFaceYNeg(block, x, y, z, glow);
		}
		if(block.shouldSideBeRendered(world, x, y, z - 1, 2) || renderer.renderAllFaces){
			renderer.renderFaceZNeg(block, x, y, z, glow);
		}
		if(block.shouldSideBeRendered(world, x + 1, y, z, 5) || renderer.renderAllFaces){
			renderer.renderFaceXPos(block, x, y, z, glow);
		}
		if(block.shouldSideBeRendered(world, x, y + 1, z, 1) || renderer.renderAllFaces){
			renderer.renderFaceYPos(block, x, y, z, glow);
		}
		if(block.shouldSideBeRendered(world, x, y, z + 1, 3) || renderer.renderAllFaces){
			renderer.renderFaceZPos(block, x, y, z, glow);
		}
		return true;
	}
}