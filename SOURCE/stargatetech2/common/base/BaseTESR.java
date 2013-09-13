package stargatetech2.common.base;

import org.lwjgl.opengl.GL11;

import stargatetech2.common.util.Vec3Int;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

@SideOnly(Side.CLIENT)
public abstract class BaseTESR extends TileEntitySpecialRenderer {
	private static final int SKY_LIGHT_CONST = 0x10000;
	protected Tessellator tessellator;

	@Override
	public final void renderTileEntityAt(TileEntity te, double x, double y, double z, float f) {
		World w = Minecraft.getMinecraft().theWorld;
		Vec3Int pos = new Vec3Int(te.xCoord, te.yCoord, te.zCoord);
		Block block = Block.blocksList[w.getBlockId(pos.x, pos.y, pos.z)];
		tessellator = Tessellator.instance;
		GL11.glPushMatrix();
		GL11.glTranslated(x+0.5D, y, z+0.5D);
		Vec3Int light = pos.offset(ForgeDirection.UNKNOWN);
		setLightCoordinates(te, light);
		float b = block.getBlockBrightness(w, light.x, light.y, light.z);
		int sky = w.getLightBrightnessForSkyBlocks(light.x, light.y, light.z, 0);
		int sky0 = sky % SKY_LIGHT_CONST;
		int sky1 = sky / SKY_LIGHT_CONST;
		tessellator.setColorOpaque_F(b, b, b);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, sky0, sky1);
		render(te, block, w, pos, f);
		GL11.glPopMatrix();
	}
	
	protected final void bindTexture(ResourceLocation texture){
		func_110628_a(texture);
	}
	
	protected void setLightCoordinates(TileEntity te, Vec3Int light){}
	
	public abstract void render(TileEntity te, Block block, World w, Vec3Int pos, float partialTicks);
}