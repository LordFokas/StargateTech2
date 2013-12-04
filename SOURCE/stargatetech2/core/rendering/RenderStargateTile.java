package stargatetech2.core.rendering;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import stargatetech2.common.base.BaseTESR;
import stargatetech2.common.reference.TextureReference;
import stargatetech2.common.util.Vec3Int;
import stargatetech2.core.tileentity.TileStargate;

public class RenderStargateTile extends BaseTESR {
	private static final int RING_SEGMENTS = 13;
	private static final int CHEVRON_COUNT = 9;
	private static final double ANGLE_RING = 1D / (double) RING_SEGMENTS;
	private static final double ANGLE_CHEV = 1D / (double) CHEVRON_COUNT;
	
	private static final float OUT_THIN_X = 0.5547F;
	private static final float OUT_WIDE_X = 0.6162F;
	private static final float OUT_THICK = 0.25F;
	private static final float OUT_MAX_Y = 2.5F;
	private static final float OUT_MIN_Y = 2.25F;
	private static final float INR_THIN_X = 0.4932F;
	private static final float INR_MIN_Y = 2.0F;
	private static final float INR_THICK = 0.18F;
	private static final float INR_THIN = 0.12F;
	
	private static final float REF_TX_X0 = 0F;
	private static final float REF_TX_X1 = 1F;
	private static final float OUT_TX_Y0 = 0F;
	private static final float OUT_TX_Y1 = 0.25F;
	private static final float OUT_TX_Y2 = 0.5F;
	private static final float OS_DELTA = 0.05F;
	private static final float INR_TX_Y0 = 0.5F;
	private static final float INR_TX_Y1 = 0.75F;
	private static final float INR_TX_Y2 = 1F;
	
	private static final float SYMBOL_S = 0.2F;
	private static final float SYMBOL_X = SYMBOL_S / 2F;
	private static final float SYMBOL_Y0 = 2.019F;
	private static final float SYMBOL_Y1 = SYMBOL_Y0 + SYMBOL_S;
	private static final float FLT = -0.005F;
	private static final float OFF = 0.3F;
	
	private static final int[][] SYMBOLS = new int[][]{
		new int[]{39,  1,  2}, //  1
		new int[]{ 3,  4,  5}, //  2
		new int[]{ 6,  7,  8}, //  3
		new int[]{ 9, 10, 11}, //  4
		new int[]{12, 13, 14}, //  5
		new int[]{15, 16, 17}, //  6
		new int[]{18, 19, 20}, //  7
		new int[]{21, 22, 23}, //  8
		new int[]{24, 25, 26}, //  9
		new int[]{27, 28, 29}, // 10
		new int[]{30, 31, 32}, // 11
		new int[]{33, 34, 35}, // 12
		new int[]{36, 37, 38}  // 13
	};
	
	@Override
	public void render(TileEntity te, Block block, World w, Vec3Int pos, float partialTicks) {
		TileStargate stargate = (TileStargate) te;
		bindTexture(TextureReference.TESR_STARGATE);
		GL11.glTranslated(0, 2.5D, 0);
		renderOuterRing();
		GL11.glPushMatrix();
			GL11.glRotatef(stargate.curr_theta + (stargate.dTheta * partialTicks), 0, 0, 1);
			renderInnerRing();
			bindTexture(TextureReference.SYMBOLS);
			renderSymbols();
		GL11.glPopMatrix();
		bindTexture(TextureReference.CHEVRONS);
		renderChevrons();
	}
	
	private void renderOuterRing(){
		GL11.glPushMatrix();
		for(int i = 0; i < RING_SEGMENTS; i++){
			renderOuterSegment();
			GL11.glRotated(ANGLE_RING * 360D, 0, 0, 360);
		}
		GL11.glPopMatrix();
	}
	
	private void renderInnerRing(){
		GL11.glPushMatrix();
		for(int i = 0; i < RING_SEGMENTS; i++){
			renderInnerSegment();
			GL11.glRotated(ANGLE_RING * 360D, 0, 0, 360);
		}
		GL11.glPopMatrix();
	}
	
	private void renderSymbols(){
		GL11.glPushMatrix();
		for(int i = 0; i < RING_SEGMENTS; i++){
			renderSymbolSegment(i);
			GL11.glRotated(ANGLE_RING * 360D, 0, 0, 360);
		}
		GL11.glPopMatrix();
	}
	
	private void renderChevrons(){
		GL11.glPushMatrix();
		for(int i = 0; i < CHEVRON_COUNT; i++){
			renderChevron();
			GL11.glRotated(ANGLE_CHEV * 360D, 0, 0, 360);
		}
		GL11.glPopMatrix();
	}
	
	private void renderOuterSegment(){
		// front
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glTexCoord2f(REF_TX_X0, OUT_TX_Y0);
		GL11.glVertex3f(-OUT_WIDE_X, OUT_MAX_Y, -OUT_THICK);
		GL11.glTexCoord2f(REF_TX_X1, OUT_TX_Y0);
		GL11.glVertex3f(OUT_WIDE_X, OUT_MAX_Y, -OUT_THICK);
		GL11.glTexCoord2f(REF_TX_X0 + OS_DELTA, OUT_TX_Y1);
		GL11.glVertex3f(-OUT_THIN_X, OUT_MIN_Y, -OUT_THICK);
		GL11.glTexCoord2f(REF_TX_X1 - OS_DELTA, OUT_TX_Y1);
		GL11.glVertex3f(OUT_THIN_X, OUT_MIN_Y, -OUT_THICK);
		GL11.glEnd();
		
		// back
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glTexCoord2f(REF_TX_X0, OUT_TX_Y0);
		GL11.glVertex3f(-OUT_WIDE_X, OUT_MAX_Y, OUT_THICK);
		GL11.glTexCoord2f(REF_TX_X0 + OS_DELTA, OUT_TX_Y1);
		GL11.glVertex3f(-OUT_THIN_X, OUT_MIN_Y, OUT_THICK);
		GL11.glTexCoord2f(REF_TX_X1, OUT_TX_Y0);
		GL11.glVertex3f(OUT_WIDE_X, OUT_MAX_Y, OUT_THICK);
		GL11.glTexCoord2f(REF_TX_X1 - OS_DELTA, OUT_TX_Y1);
		GL11.glVertex3f(OUT_THIN_X, OUT_MIN_Y, OUT_THICK);
		GL11.glEnd();
		
		// inner
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glTexCoord2f(REF_TX_X0 + OS_DELTA, OUT_TX_Y1);
		GL11.glVertex3f(-OUT_THIN_X, OUT_MIN_Y, OUT_THICK);
		GL11.glTexCoord2f(REF_TX_X0 + OS_DELTA, OUT_TX_Y2);
		GL11.glVertex3f(-OUT_THIN_X, OUT_MIN_Y, -OUT_THICK);
		GL11.glTexCoord2f(REF_TX_X1 - OS_DELTA, OUT_TX_Y1);
		GL11.glVertex3f(OUT_THIN_X, OUT_MIN_Y, OUT_THICK);
		GL11.glTexCoord2f(REF_TX_X1 - OS_DELTA, OUT_TX_Y2);
		GL11.glVertex3f(OUT_THIN_X, OUT_MIN_Y, -OUT_THICK);
		GL11.glEnd();
		
		//outer
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glTexCoord2f(REF_TX_X0, OUT_TX_Y0);
		GL11.glVertex3f(-OUT_WIDE_X, OUT_MAX_Y, OUT_THICK);
		GL11.glTexCoord2f(REF_TX_X1, OUT_TX_Y0);
		GL11.glVertex3f(OUT_WIDE_X, OUT_MAX_Y, OUT_THICK);
		GL11.glTexCoord2f(REF_TX_X0, OUT_TX_Y1);
		GL11.glVertex3f(-OUT_WIDE_X, OUT_MAX_Y, -OUT_THICK);
		GL11.glTexCoord2f(REF_TX_X1, OUT_TX_Y1);
		GL11.glVertex3f(OUT_WIDE_X, OUT_MAX_Y, -OUT_THICK);
		GL11.glEnd();
	}
	
	private void renderInnerSegment(){
		// front
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glTexCoord2f(REF_TX_X0 + OS_DELTA, INR_TX_Y0);
		GL11.glVertex3f(-INR_THIN_X, INR_MIN_Y, -INR_THIN);
		GL11.glTexCoord2f(REF_TX_X0, INR_TX_Y1);
		GL11.glVertex3f(-OUT_THIN_X, OUT_MIN_Y, -INR_THICK);
		GL11.glTexCoord2f(REF_TX_X1 - OS_DELTA, INR_TX_Y0);
		GL11.glVertex3f(INR_THIN_X, INR_MIN_Y, -INR_THIN);
		GL11.glTexCoord2f(REF_TX_X1, INR_TX_Y1);
		GL11.glVertex3f(OUT_THIN_X, OUT_MIN_Y, -INR_THICK);
		GL11.glEnd();
		
		//back
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glTexCoord2f(REF_TX_X0 + OS_DELTA, INR_TX_Y0);
		GL11.glVertex3f(-INR_THIN_X, INR_MIN_Y, INR_THIN);
		GL11.glTexCoord2f(REF_TX_X1 - OS_DELTA, INR_TX_Y0);
		GL11.glVertex3f(INR_THIN_X, INR_MIN_Y, INR_THIN);
		GL11.glTexCoord2f(REF_TX_X0, INR_TX_Y1);
		GL11.glVertex3f(-OUT_THIN_X, OUT_MIN_Y, INR_THICK);
		GL11.glTexCoord2f(REF_TX_X1, INR_TX_Y1);
		GL11.glVertex3f(OUT_THIN_X, OUT_MIN_Y, INR_THICK);
		GL11.glEnd();
		
		// inner
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glTexCoord2f(REF_TX_X0 + OS_DELTA, INR_TX_Y1);
		GL11.glVertex3f(-INR_THIN_X, INR_MIN_Y, INR_THIN);
		GL11.glTexCoord2f(REF_TX_X0 + OS_DELTA, INR_TX_Y2);
		GL11.glVertex3f(-INR_THIN_X, INR_MIN_Y, -INR_THIN);
		GL11.glTexCoord2f(REF_TX_X1 - OS_DELTA, INR_TX_Y1);
		GL11.glVertex3f(INR_THIN_X, INR_MIN_Y, INR_THIN);
		GL11.glTexCoord2f(REF_TX_X1 - OS_DELTA, INR_TX_Y2);
		GL11.glVertex3f(INR_THIN_X, INR_MIN_Y, -INR_THIN);
		GL11.glEnd();
	}
	
	private void renderSymbolSegment(int sgmt){
		int[] symbols = SYMBOLS[sgmt];
		SymbolCoordinates lft = SymbolCoordinates.get(symbols[0]);
		SymbolCoordinates ctr = SymbolCoordinates.get(symbols[1]);
		SymbolCoordinates rgt = SymbolCoordinates.get(symbols[2]);
		
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glTexCoord2f(lft.x0, lft.y1);
		GL11.glVertex3f(-SYMBOL_X + OFF, SYMBOL_Y0, -INR_THIN + FLT);
		GL11.glTexCoord2f(lft.x0, lft.y0);
		GL11.glVertex3f(-SYMBOL_X + OFF, SYMBOL_Y1, -INR_THICK + FLT);
		GL11.glTexCoord2f(lft.x1, lft.y1);
		GL11.glVertex3f(SYMBOL_X + OFF, SYMBOL_Y0, -INR_THIN + FLT);
		GL11.glTexCoord2f(lft.x1, lft.y0);
		GL11.glVertex3f(SYMBOL_X + OFF, SYMBOL_Y1, -INR_THICK + FLT);
		GL11.glEnd();
		
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glTexCoord2f(ctr.x0, ctr.y1);
		GL11.glVertex3f(-SYMBOL_X, SYMBOL_Y0, -INR_THIN + FLT);
		GL11.glTexCoord2f(ctr.x0, ctr.y0);
		GL11.glVertex3f(-SYMBOL_X, SYMBOL_Y1, -INR_THICK + FLT);
		GL11.glTexCoord2f(ctr.x1, ctr.y1);
		GL11.glVertex3f(SYMBOL_X, SYMBOL_Y0, -INR_THIN + FLT);
		GL11.glTexCoord2f(ctr.x1, ctr.y0);
		GL11.glVertex3f(SYMBOL_X, SYMBOL_Y1, -INR_THICK + FLT);
		GL11.glEnd();
		
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glTexCoord2f(rgt.x0, rgt.y1);
		GL11.glVertex3f(-SYMBOL_X - OFF, SYMBOL_Y0, -INR_THIN + FLT);
		GL11.glTexCoord2f(rgt.x0, rgt.y0);
		GL11.glVertex3f(-SYMBOL_X - OFF, SYMBOL_Y1, -INR_THICK + FLT);
		GL11.glTexCoord2f(rgt.x1, rgt.y1);
		GL11.glVertex3f(SYMBOL_X - OFF, SYMBOL_Y0, -INR_THIN + FLT);
		GL11.glTexCoord2f(rgt.x1, rgt.y0);
		GL11.glVertex3f(SYMBOL_X - OFF, SYMBOL_Y1, -INR_THICK + FLT);
		GL11.glEnd();
	}
	
	private static final float CHT_X0 = 0F / 3F;
	private static final float CHT_X1 = 1F / 3F;
	private static final float CHT_X2 = 2F / 3F;
	private static final float CHT_X3 = 3F / 3F;
	private static final float CHT_Y0 = 0;
	private static final float CHT_Y1 = 1;
	private static final float CH_Z0 = -0.25F;
	private static final float CH_Z1 = -0.30F;
	
	private static final float CH_YO = 2.5F;
	private static final float CH_YM = 2.3F;
	
	private static final float CH_XO = 0.12F;
	private static final float CH_XI = 0.06F;
	
	private void renderChevron(){
		renderChevronLight();
		renderChevronArm();
	}
	
	private void renderChevronLight(){
		//light
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glTexCoord2f(CHT_X1, CHT_Y0);
		GL11.glVertex3f(-CH_XI, CH_YM, CH_Z1);
		GL11.glTexCoord2f(CHT_X1, CHT_Y1);
		GL11.glVertex3f(-CH_XO, CH_YO, CH_Z1);
		GL11.glTexCoord2f(CHT_X2, CHT_Y0);
		GL11.glVertex3f(CH_XI, CH_YM, CH_Z1);
		GL11.glTexCoord2f(CHT_X2, CHT_Y1);
		GL11.glVertex3f(CH_XO, CH_YO, CH_Z1);
		GL11.glEnd();
		
		// back
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glTexCoord2f(CHT_X0, CHT_Y0);
		GL11.glVertex3f(-CH_XI, CH_YM, CH_Z0);
		GL11.glTexCoord2f(CHT_X1, CHT_Y0);
		GL11.glVertex3f(CH_XI, CH_YM, CH_Z0);
		GL11.glTexCoord2f(CHT_X0, CHT_Y1);
		GL11.glVertex3f(-CH_XO, CH_YO, CH_Z0);
		GL11.glTexCoord2f(CHT_X1, CHT_Y1);
		GL11.glVertex3f(CH_XO, CH_YO, CH_Z0);
		GL11.glEnd();
		
		//bottom
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glTexCoord2f(CHT_X0, CHT_Y0);
		GL11.glVertex3f(-CH_XI, CH_YM, CH_Z1);
		GL11.glTexCoord2f(CHT_X1, CHT_Y0);
		GL11.glVertex3f(CH_XI, CH_YM, CH_Z1);
		GL11.glTexCoord2f(CHT_X0, CHT_Y1);
		GL11.glVertex3f(-CH_XI, CH_YM, CH_Z0);
		GL11.glTexCoord2f(CHT_X1, CHT_Y1);
		GL11.glVertex3f(CH_XI, CH_YM, CH_Z0);
		GL11.glEnd();
		
		// top
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glTexCoord2f(CHT_X0, CHT_Y0);
		GL11.glVertex3f(-CH_XO, CH_YO, CH_Z1);
		GL11.glTexCoord2f(CHT_X0, CHT_Y1);
		GL11.glVertex3f(-CH_XO, CH_YO, CH_Z0);
		GL11.glTexCoord2f(CHT_X1, CHT_Y0);
		GL11.glVertex3f(CH_XO, CH_YO, CH_Z1);
		GL11.glTexCoord2f(CHT_X1, CHT_Y1);
		GL11.glVertex3f(CH_XO, CH_YO, CH_Z0);
		GL11.glEnd();
		
		// right
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glTexCoord2f(CHT_X0, CHT_Y0);
		GL11.glVertex3f(-CH_XI, CH_YM, CH_Z0);
		GL11.glTexCoord2f(CHT_X0, CHT_Y1);
		GL11.glVertex3f(-CH_XO, CH_YO, CH_Z0);
		GL11.glTexCoord2f(CHT_X1, CHT_Y0);
		GL11.glVertex3f(-CH_XI, CH_YM, CH_Z1);
		GL11.glTexCoord2f(CHT_X1, CHT_Y1);
		GL11.glVertex3f(-CH_XO, CH_YO, CH_Z1);
		GL11.glEnd();
		
		//left
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glTexCoord2f(CHT_X0, CHT_Y0);
		GL11.glVertex3f(CH_XI, CH_YM, CH_Z0);
		GL11.glTexCoord2f(CHT_X1, CHT_Y0);
		GL11.glVertex3f(CH_XI, CH_YM, CH_Z1);
		GL11.glTexCoord2f(CHT_X0, CHT_Y1);
		GL11.glVertex3f(CH_XO, CH_YO, CH_Z0);
		GL11.glTexCoord2f(CHT_X1, CHT_Y1);
		GL11.glVertex3f(CH_XO, CH_YO, CH_Z1);
		GL11.glEnd();
	}
	
	private void renderChevronArm(){
		
	}
}