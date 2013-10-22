package stargatetech2.core.rendering;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import stargatetech2.common.base.BaseISBRH;
import stargatetech2.common.reference.TextureReference;
import stargatetech2.common.util.IconRegistry;

public class RenderNaquadahOre extends BaseISBRH {
	private static final RenderNaquadahOre INSTANCE = new RenderNaquadahOre();
	
	public static RenderNaquadahOre instance(){
		return INSTANCE;
	}
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer){
		Icon face = IconRegistry.blockIcons.get(TextureReference.FACE_NAQUADAH_ORE);
		Icon glow = IconRegistry.blockIcons.get(TextureReference.GLOW_NAQUADAH_ORE);
		renderer.setOverrideBlockTexture(face);
		renderer.renderStandardBlock(block, x, y, z);
		renderer.clearOverrideBlockTexture();
		Tessellator tessellator = Tessellator.instance;
		tessellator.setColorOpaque_I(0xFFFFFF);
		tessellator.setBrightness(220);
		renderer.renderFaceXNeg(block, x, y, z, glow);
		renderer.renderFaceYNeg(block, x, y, z, glow);
		renderer.renderFaceZNeg(block, x, y, z, glow);
		renderer.renderFaceXPos(block, x, y, z, glow);
		renderer.renderFaceYPos(block, x, y, z, glow);
		renderer.renderFaceZPos(block, x, y, z, glow);	
		return true;
	}
}