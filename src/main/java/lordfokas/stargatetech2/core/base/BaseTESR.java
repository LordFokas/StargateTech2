package lordfokas.stargatetech2.core.base;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import lordfokas.stargatetech2.core.ModuleCore;
import lordfokas.stargatetech2.core.util.Vec3Int;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class BaseTESR extends TileEntitySpecialRenderer {
	private static final int SKY_LIGHT_CONST = 0x10000;
	protected Tessellator tessellator;

	@Override
	public final void renderTileEntityAt(TileEntity te, double x, double y, double z, float f) {
		World w = Minecraft.getMinecraft().theWorld;
		Vec3Int pos = new Vec3Int(te.xCoord, te.yCoord, te.zCoord);
		Block block = w.getBlock(pos.x, pos.y, pos.z);
		if(block == null){
			// Fallback to a known block instance in case
			// rendering starts before the world fully loads.
			block = ModuleCore.naquadahBlock;
		}
		tessellator = Tessellator.instance;
		GL11.glPushMatrix();
		GL11.glTranslated(x+0.5D, y, z+0.5D);
		if(useLocalizedLighting()){
			Vec3Int light = pos.offset(ForgeDirection.UNKNOWN);
			light = getLightCoordinates(te, light);
			// float b = block.getBlockBrightness(w, light.x, light.y, light.z);
			float b = block.getMixedBrightnessForBlock(w, light.x, light.y, light.z);
			int sky = w.getLightBrightnessForSkyBlocks(light.x, light.y, light.z, 0);
			int sky0 = sky % SKY_LIGHT_CONST;
			int sky1 = sky / SKY_LIGHT_CONST;
			tessellator.setColorOpaque_F(b, b, b);
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, sky0, sky1);
		}
		render(te, block, w, pos, f);
		GL11.glPopMatrix();
	}
	
	protected Vec3Int getLightCoordinates(TileEntity te, Vec3Int light){ return light; }
	protected boolean useLocalizedLighting(){ return false; }
	
	public abstract void render(TileEntity te, Block block, World w, Vec3Int pos, float partialTicks);
}