package stargatetech2.common.base;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import stargatetech2.common.base.BaseGUI.IGauge;
import stargatetech2.common.base.BaseGUI.IHoverHandler;
import stargatetech2.core.util.IonizedParticles;

public abstract class BaseGauge implements IGauge{
	protected BaseGUI master;
	protected GaugeHoverHandler hoverHandler;
	protected float cVal, mVal;
	protected int xPos, yPos;
	
	protected class GaugeHoverHandler implements IHoverHandler{
		public boolean isHover = false;
		public int hoverX, hoverY;
		
		@Override
		public void onHover(int x, int y) {
			isHover = true;
			hoverX = x;
			hoverY = y;
		}

		@Override
		public void onLeave() {
			isHover = false;
		}
		
	}
	
	protected void bindImage(ResourceLocation rl){
		Minecraft.getMinecraft().renderEngine.bindTexture(rl);;
	}
	
	public BaseGauge(int x, int y, float maxValue){
		hoverHandler = new GaugeHoverHandler();
		mVal = maxValue;
		cVal = 0F;
		xPos = x;
		yPos = y;
	}
	
	@Override
	public void register(BaseGUI gui){
		master = gui;
		master.addHoverHandler(hoverHandler, xPos, yPos, 16, 64);
	}
	
	public void setCurrentValue(float currentValue){
		cVal = currentValue;
	}
	
	public static class TankGauge extends BaseGauge{
		public TankGauge(int x, int y, float maxValue) {
			super(x, y, maxValue);
		}

		@Override
		public void renderGauge() {
			float fill = cVal / mVal;
			Icon f = IonizedParticles.fluid.getIcon();
			bindImage(TextureMap.locationBlocksTexture);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			if(fill > 0.75F) master.drawQuad(xPos, yPos,	f.getMinU(), f.getMaxU(), f.getMinV(), f.getMaxV(), 16, 16);
			if(fill > 0.50F) master.drawQuad(xPos, yPos+16, f.getMinU(), f.getMaxU(), f.getMinV(), f.getMaxV(), 16, 16);
			if(fill > 0.25F) master.drawQuad(xPos, yPos+32, f.getMinU(), f.getMaxU(), f.getMinV(), f.getMaxV(), 16, 16);
			if(fill > 0.00F) master.drawQuad(xPos, yPos+48, f.getMinU(), f.getMaxU(), f.getMinV(), f.getMaxV(), 16, 16);
			fill = 1F - fill;
			master.bindBGImage();
			master.drawLocalQuad(xPos, yPos, xPos, xPos+16, yPos, yPos + (64F * fill), 16, 64F*fill);
			master.bindBaseImage();
			master.drawLocalQuad(xPos, yPos, 80, 96, 0, 64, 16, 64);
		}
		
		@Override
		public void renderTooltip() {
			if(hoverHandler.isHover){
				int baseX = hoverHandler.hoverX+2;
				int baseY = hoverHandler.hoverY-26;
				String str = String.format("%d / %d", (int)cVal, (int)mVal);
				master.bindBaseImage();
				master.drawLocalQuad(baseX, baseY, 0, 96, 64, 88, 96, 24);
				master.drawLeft("Ionized Particles", baseX+4, baseY+3, 0x444444);
				master.drawLeft(str, baseX+4, baseY+14, 0x444444);
			}
		}
	}
	
	public static class PowerGauge extends BaseGauge{
		public PowerGauge(int x, int y, float maxValue) {
			super(x, y, maxValue);
		}

		@Override
		public void renderGauge() {
			float power = cVal / mVal;
			master.bindBaseImage();
			master.drawLocalQuad(xPos, yPos + (64F * (1F-power)), 64, 80, (64F - (64F * power)), 64, 16, 64F*power);
		}

		@Override
		public void renderTooltip() {
			if(hoverHandler.isHover){
				int baseX = hoverHandler.hoverX+2;
				int baseY = hoverHandler.hoverY-26;
				String str = String.format("%d / %d", (int)cVal, (int)mVal);
				master.bindBaseImage();
				master.drawLocalQuad(baseX, baseY, 0, 96, 64, 88, 96, 24);
				master.drawLeft("Power", baseX+4, baseY+3, 0x444444);
				master.drawLeft(str, baseX+4, baseY+14, 0x444444);
			}
		}
	}
}