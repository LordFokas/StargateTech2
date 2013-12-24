package stargatetech2.core.rendering;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import stargatetech2.common.base.BaseTESR;
import stargatetech2.common.reference.TextureReference;
import stargatetech2.common.util.Vec3Int;
import stargatetech2.core.tileentity.TileTransportRing;
import stargatetech2.core.tileentity.TileTransportRing.RingRenderData;

public class RenderTransportRing extends BaseTESR{
	private static final double ANGLE = 1.0D / 9.0D;
	
	private static final float  INNER_X = 1.8F;
	private static final float OUTTER_X = 2.3F;
	private static final float    MIN_Y = 0.2F;
	private static final float    MAX_Y = 0.5F;
	private static final float  INNER_Z = 0.656F;
	private static final float OUTTER_Z = 0.838F;
	
	private static final float TEXTURE_SIZE = 48.0F;
	private static final float X_BEGIN0 = 00.0F / TEXTURE_SIZE;
	private static final float X_FINAL0 = 33.0F / TEXTURE_SIZE;
	private static final float Y_BEGIN0 = 00.0F / TEXTURE_SIZE;
	private static final float Y_FINAL0 = 10.0F / TEXTURE_SIZE;
	private static final float Y_BEGIN1 = 11.0F / TEXTURE_SIZE;
	private static final float Y_FINAL1 = 21.0F / TEXTURE_SIZE;
	private static final float Y_BEGIN2 = 22.0F / TEXTURE_SIZE;
	private static final float Y_FINAL2 = 32.0F / TEXTURE_SIZE;
	
	private int displayList;
	private boolean compiled = false;
	
	@Override
	protected Vec3Int getLightCoordinates(TileEntity te, Vec3Int light){
		return new Vec3Int(light.x, light.y + 2, light.z);
	}
	
	@Override
	protected boolean useLocalizedLighting(){
		return true;
	}
	
	@Override
	public void render(TileEntity te, Block block, World w, Vec3Int pos, float partialTicks) {
		RingRenderData data = ((TileTransportRing)te).renderData;
		if(!data.isRendering) return;
		bindTexture(TextureReference.TESR_TRANSPORT_RING);
		GL11.glTranslated(0, 2.0D, 0);
		for(int r = 0; r < 5; r++){
			int ringPos = data.ringPos[r];
			if(ringPos != 0){
				double fPos;
				if(ringPos == TileTransportRing.RING_MOV){
					fPos = ((double)TileTransportRing.RING_MOV);
				}else{
					fPos = ((double)ringPos) + ((double)(partialTicks * data.mode));
				}
				double translation = ((double)r) * 0.6D * (fPos / ((double)TileTransportRing.RING_MOV));
				GL11.glPushMatrix();
				GL11.glTranslated(0, translation, 0);
				renderRing();
				GL11.glPopMatrix();
			}
		}
	}
	
	private void renderRing(){
		for(int i = 0; i < 9; i++){
			GL11.glRotated(ANGLE * 360D, 0, 360, 0);
			if(!compiled){
				displayList = GLAllocation.generateDisplayLists(1);
				GL11.glNewList(displayList, GL11.GL_COMPILE_AND_EXECUTE);
				renderSegment();
				GL11.glEndList();
				compiled = true;
			}else{
				GL11.glCallList(displayList);
			}
		}
	}
	
	private void renderSegment(){
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glTexCoord2f(X_BEGIN0, Y_BEGIN0);
		GL11.glVertex3f(INNER_X, MAX_Y, -INNER_Z);
		GL11.glTexCoord2f(X_FINAL0, Y_BEGIN0);
		GL11.glVertex3f(INNER_X, MAX_Y, INNER_Z);
		GL11.glTexCoord2f(X_BEGIN0, Y_FINAL0);
		GL11.glVertex3f(OUTTER_X, MAX_Y, -OUTTER_Z);
		GL11.glTexCoord2f(X_FINAL0, Y_FINAL0);
		GL11.glVertex3f(OUTTER_X, MAX_Y, OUTTER_Z);
		GL11.glEnd();
		
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glTexCoord2f(X_BEGIN0, Y_BEGIN0);
		GL11.glVertex3f(INNER_X, MIN_Y, -INNER_Z);
		GL11.glTexCoord2f(X_BEGIN0, Y_FINAL0);
		GL11.glVertex3f(OUTTER_X, MIN_Y, -OUTTER_Z);
		GL11.glTexCoord2f(X_FINAL0, Y_BEGIN0);
		GL11.glVertex3f(INNER_X, MIN_Y, INNER_Z);
		GL11.glTexCoord2f(X_FINAL0, Y_FINAL0);
		GL11.glVertex3f(OUTTER_X, MIN_Y, OUTTER_Z);
		GL11.glEnd();
		
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glTexCoord2f(X_BEGIN0, Y_BEGIN1);
		GL11.glVertex3f(OUTTER_X, MIN_Y, -OUTTER_Z);
		GL11.glTexCoord2f(X_BEGIN0, Y_FINAL1);
		GL11.glVertex3f(OUTTER_X, MAX_Y, -OUTTER_Z);
		GL11.glTexCoord2f(X_FINAL0, Y_BEGIN1);
		GL11.glVertex3f(OUTTER_X, MIN_Y, OUTTER_Z);
		GL11.glTexCoord2f(X_FINAL0, Y_FINAL1);
		GL11.glVertex3f(OUTTER_X, MAX_Y, OUTTER_Z);
		GL11.glEnd();
		
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glTexCoord2f(X_BEGIN0, Y_BEGIN2);
		GL11.glVertex3f(INNER_X, MIN_Y, -INNER_Z);
		GL11.glTexCoord2f(X_FINAL0, Y_BEGIN2);
		GL11.glVertex3f(INNER_X, MIN_Y, INNER_Z);
		GL11.glTexCoord2f(X_BEGIN0, Y_FINAL2);
		GL11.glVertex3f(INNER_X, MAX_Y, -INNER_Z);
		GL11.glTexCoord2f(X_FINAL0, Y_FINAL2);
		GL11.glVertex3f(INNER_X, MAX_Y, INNER_Z);
		GL11.glEnd();
	}
}