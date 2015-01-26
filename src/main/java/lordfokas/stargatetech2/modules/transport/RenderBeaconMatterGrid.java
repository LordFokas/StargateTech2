package lordfokas.stargatetech2.modules.transport;

import java.nio.FloatBuffer;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class RenderBeaconMatterGrid extends TileEntitySpecialRenderer{
	private static final ResourceLocation ender_sky = new ResourceLocation("textures/environment/end_sky.png");
    private static final ResourceLocation end_portal = new ResourceLocation("textures/entity/end_portal.png");
    private static final Random random = new Random(31100L);
    private FloatBuffer buffer = GLAllocation.createDirectFloatBuffer(16);
	
	@Override
	public final void renderTileEntityAt(TileEntity te, double x, double y, double z, float f){
		float tl_x = (float)this.field_147501_a.field_147560_j;
        float tl_y = (float)this.field_147501_a.field_147561_k;
        float tl_z = (float)this.field_147501_a.field_147558_l;
        GL11.glDisable(GL11.GL_LIGHTING);
        random.setSeed(31100L);
        float y_offset = 0.94F;

        for (int layer = 0; layer < 16; ++layer){
            GL11.glPushMatrix();
            float layer_off = (float)(16 - layer);
            float scale = 0.0625F;
            float color_mult = 1.0F / (layer_off + 1.0F);
            
            if (layer == 0){
                this.bindTexture(ender_sky);
                color_mult = 0.1F;
                layer_off = 65.0F;
                scale = 0.125F;
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            }
            
            if (layer == 1){
                this.bindTexture(end_portal);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
                scale = 0.5F;
            }
            
            float f8 = (float)(-(y + (double)y_offset));
            float f9 = f8 + ActiveRenderInfo.objectY;
            float f10 = f8 + layer_off + ActiveRenderInfo.objectY;
            float f11 = f9 / f10;
            f11 += (float)(y + (double)y_offset);
            
            GL11.glTranslatef(tl_x, f11, tl_z);
            GL11.glTexGeni(GL11.GL_S, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);
            GL11.glTexGeni(GL11.GL_T, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);
            GL11.glTexGeni(GL11.GL_R, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);
            GL11.glTexGeni(GL11.GL_Q, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_EYE_LINEAR);
            GL11.glTexGen(GL11.GL_S, GL11.GL_OBJECT_PLANE, floatBuffer(1.0F, 0.0F, 0.0F, 0.0F));
            GL11.glTexGen(GL11.GL_T, GL11.GL_OBJECT_PLANE, floatBuffer(0.0F, 0.0F, 1.0F, 0.0F));
            GL11.glTexGen(GL11.GL_R, GL11.GL_OBJECT_PLANE, floatBuffer(0.0F, 0.0F, 0.0F, 1.0F));
            GL11.glTexGen(GL11.GL_Q, GL11.GL_EYE_PLANE, floatBuffer(0.0F, 1.0F, 0.0F, 0.0F));
            GL11.glEnable(GL11.GL_TEXTURE_GEN_S);
            GL11.glEnable(GL11.GL_TEXTURE_GEN_T);
            GL11.glEnable(GL11.GL_TEXTURE_GEN_R);
            GL11.glEnable(GL11.GL_TEXTURE_GEN_Q);
            GL11.glPopMatrix();
            GL11.glMatrixMode(GL11.GL_TEXTURE);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glTranslatef(0.0F, (float)(Minecraft.getSystemTime() % 700000L) / 700000.0F, 0.0F);
            GL11.glScalef(scale, scale, scale);
            GL11.glTranslatef(0.5F, 0.5F, 0.0F);
            GL11.glRotatef((float)(layer * layer * 4321 + layer * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
            GL11.glTranslatef(-tl_x, -tl_z, -tl_y);
            f9 = f8 + ActiveRenderInfo.objectY;
            GL11.glTranslatef(ActiveRenderInfo.objectX * layer_off / f9, ActiveRenderInfo.objectZ * layer_off / f9, -tl_y);
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            f11 = random.nextFloat() * 0.5F + 0.1F;
            float f12 = random.nextFloat() * 0.5F + 0.4F;
            float f13 = random.nextFloat() * 0.5F + 0.5F;
            if (layer == 0){
                f13 = 1.0F;
                f12 = 1.0F;
                f11 = 1.0F;
            }
            tessellator.setColorRGBA_F(f11 * color_mult, f12 * color_mult, f13 * color_mult, 1.0F);
            tessellator.addVertex(x, y + (double)y_offset, z);
            tessellator.addVertex(x, y + (double)y_offset, z + 1.0D);
            tessellator.addVertex(x + 1.0D, y + (double)y_offset, z + 1.0D);
            tessellator.addVertex(x + 1.0D, y + (double)y_offset, z);
            tessellator.draw();
            GL11.glPopMatrix();
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_S);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_T);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_R);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_Q);
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    private FloatBuffer floatBuffer(float w, float x, float y, float z){
        buffer.clear();
        buffer.put(w).put(x).put(y).put(z);
        buffer.flip();
        return buffer;
    }
}