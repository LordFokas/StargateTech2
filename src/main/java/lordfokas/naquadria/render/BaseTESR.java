package lordfokas.naquadria.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class BaseTESR extends TileEntitySpecialRenderer {
	private static final int SKY_LIGHT_CONST = 0x10000;
	protected Tessellator tessellator;
	
	@Override
	public final void renderTileEntityAt(TileEntity te, double x, double y, double z, float f, int stage) {
		World w = Minecraft.getMinecraft().theWorld;
		BlockPos pos = new BlockPos(x, y, z);
		IBlockState state = w.getBlockState(pos);
		if(state == null) return;
		Block block = state.getBlock();
		if(block == null) return;
		GL11.glPushMatrix();
		GL11.glTranslated(x+0.5D, y, z+0.5D);
		GL11.glColor3f(1, 1, 1);
		if(useLocalizedLighting()){
			tessellator = Tessellator.getInstance();
			BlockPos light = new BlockPos(x, y, z);
			light = getLightCoordinates(te, light);
			// TODO: check consistency and cleanup
			// float b = block.getMixedBrightnessForBlock(w, light);
			float b = w.getLightBrightness(light);
			int sky = w.getCombinedLight(light, 0);
			int sky0 = sky % SKY_LIGHT_CONST;
			int sky1 = sky / SKY_LIGHT_CONST;
			// tessellator.setColorOpaque_F(b, b, b); // FIXME: find the new method for this
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, sky0, sky1);
		}
		render(te, block, w, pos, f);
		GL11.glPopMatrix();
	}
	
	protected BlockPos getLightCoordinates(TileEntity te, BlockPos light){ return light; }
	protected boolean useLocalizedLighting(){ return false; }
	
	public abstract void render(TileEntity te, Block block, World w, BlockPos pos, float partialTicks);
}