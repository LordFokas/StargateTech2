package stargatetech2.common.base;

import java.util.ArrayList;

import net.java.games.input.Keyboard;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import stargatetech2.common.reference.TextureReference;

public abstract class BaseGUI extends GuiContainer {
	private boolean onBackground;
	private boolean isNativeRender;
	private boolean usesTextHandler = false;
	protected ResourceLocation bgImage = null;
	
	private ArrayList<IGauge> gauges = new ArrayList<IGauge>();
	private ArrayList<HandlerWrapper<IClickHandler>> clickHandlers = new ArrayList<HandlerWrapper<IClickHandler>>();
	private ArrayList<HandlerWrapper<IHoverHandler>> hoverHandlers = new ArrayList<HandlerWrapper<IHoverHandler>>();
	
	public static interface IGauge{
		public void register(BaseGUI gui);
		public void renderGauge();
		public void renderTooltip();
	}
	
	public static interface IClickHandler{
		public void onClick(int x, int y);
	}
	
	public static interface IHoverHandler{
		public void onHover(int x, int y);
		public void onLeave();
	}
	
	private static class HandlerWrapper<H>{
		public final H handler;
		public final int minX, minY, maxX, maxY;
		
		public HandlerWrapper(H h, int x0, int y0, int x1, int y1){
			handler = h;
			minX = x0;
			minY = y0;
			maxX = x1;
			maxY = y1;
		}
	}
	
	public class TextHandler{
		private char[] chars = new char[64];
		private int pos = 0;

		public void onKey(char k, int c){
			if(c == 14){
				chars[pos] = ' ';
				if(pos > 0) pos--;
			}else if(c >= 2 && c <= 11 || c >= 16 && c <= 25 || c >= 30 && c <= 38 || c >= 44 && c <= 50){
				if(pos < 64){
					chars[pos] = k;
					pos++;
				}
			}
		}
		
		public String getString(int count){
			int start = count < pos ? pos-count : 0;
			StringBuffer buff = new StringBuffer();
			for(int i = start; i < pos; i++){
				buff.append(chars[i]);
			}
			return buff.toString();
		}
	}
	
	protected BaseGUI(BaseContainer container, int x, int y, boolean useText) {
		super(container != null ? container : new BaseContainer());
		xSize = x + 18;
		ySize = y + 18;
		usesTextHandler = useText;
	}
	
	protected void addClickHandler(IClickHandler handler, int x, int y, int xS, int yS){
		clickHandlers.add(new HandlerWrapper<IClickHandler>(handler, x, y, x+xS, y+yS));
	}
	
	protected void addHoverHandler(IHoverHandler handler, int x, int y, int xS, int yS){
		hoverHandlers.add(new HandlerWrapper<IHoverHandler>(handler, x, y, x+xS, y+yS));
	}
	
	protected void addGauge(IGauge gauge){
		gauge.register(this);
		gauges.add(gauge);
	}
	
	@Override
	protected final void mouseClicked(int x, int y, int btn){
		super.mouseClicked(x, y, btn);
		if(btn == 0){
			x -= (guiLeft + 9);
			y -= (guiTop + 9);
			for(HandlerWrapper<IClickHandler> hw : clickHandlers){
				if(hw.minX <= x && hw.maxX > x && hw.minY <= y && hw.maxY > y){
					hw.handler.onClick(x, y);
				}
			}
		}
	}
	
	@Override
	public final void handleMouseInput(){
		super.handleMouseInput();
		int x = Mouse.getEventX() * this.width / this.mc.displayWidth;
		int y = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
		if(Mouse.getEventButton() == -1){
			x -= (guiLeft + 9);
			y -= (guiTop + 9);
			for(HandlerWrapper<IHoverHandler> hw : hoverHandlers){
				if(hw.minX <= x && hw.maxX > x && hw.minY <= y && hw.maxY > y){
					hw.handler.onHover(x, y);
				}else{
					hw.handler.onLeave();
				}
			}
		}
		processMouseEvents();
	}
	
	protected void processMouseEvents(){}
	
	@Override
	protected final void keyTyped(char key, int code){
		if(usesTextHandler){
			if(code == 1){
				this.mc.thePlayer.closeScreen();
			}else{
				onKeyTyped(key, code);
			}
		}else if(code == 1 || code == mc.gameSettings.keyBindInventory.keyCode){
			this.mc.thePlayer.closeScreen();
		}
	}
	
	protected void onKeyTyped(char key, int code){}
	
	@Override
	protected final void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		onBackground = true;
		if(bgImage != null){
			bindImage(bgImage);
			int x = xSize - 18;
			int y = ySize - 18;
			drawLocalQuad(0, 0, 0, x, 0, y, x, y);
		}
		bindBaseImage();
		isNativeRender = true;
		drawLocalQuad(0, 0, 0, 24, 0, 24, 24, 24);
		drawLocalQuad(xSize-24, 0, 40, 64, 0, 24, 24, 24);
		drawLocalQuad(0, ySize-24, 0, 24, 40, 64, 24, 24);
		drawLocalQuad(xSize-24, ySize-24, 40, 64, 40, 64, 24, 24);
		drawLocalQuad(24, 0, 24, 40, 0, 9, xSize-48, 9);
		drawLocalQuad(24, ySize-9, 24, 40, 55, 64, xSize-48, 9);
		drawLocalQuad(0, 24, 0, 9, 24, 40, 9, ySize-48);
		drawLocalQuad(xSize-9, 24, 55, 64, 24, 40, 9, ySize-48);
		isNativeRender = false;
		updateGauges();
		for(IGauge gauge : gauges){
			gauge.renderGauge();
		}
		drawBackground();
		onBackground = false;
	}
	
	@Override
	protected final void drawGuiContainerForegroundLayer(int par1, int par2){
		drawForeground();
		for(IGauge gauge : gauges){
			gauge.renderTooltip();
		}
	}
	
	protected void updateGauges(){}
	protected void drawBackground(){}
	protected void drawForeground(){}
	
	public final void drawLocalQuad(float x, float y, float xMin, float xMax, float yMin, float yMax, float xStep, float yStep){
		drawQuad(x, y, xMin / 256F, xMax / 256F, yMin / 256F, yMax / 256F, xStep, yStep);
	}
	
	public final void drawQuad(float x, float y, float xMin, float xMax, float yMin, float yMax, float xStep, float yStep){
		if(!isNativeRender){
			x += 9;
			y += 9;
		}
		if(onBackground){
			x += guiLeft;
			y += guiTop;
		}
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glTexCoord3f(xMin, yMin, zLevel);
		GL11.glVertex2f(x, y);
		GL11.glTexCoord3f(xMin, yMax, zLevel);
		GL11.glVertex2f(x, y+yStep);
		GL11.glTexCoord3f(xMax, yMin, zLevel);
		GL11.glVertex2f(x+xStep, y);
		GL11.glTexCoord3f(xMax, yMax, zLevel);
		GL11.glVertex2f(x+xStep, y+yStep);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_LIGHTING);
	}
	
	protected void bindImage(ResourceLocation rl){
		mc.renderEngine.func_110577_a(rl);
	}
	
	public void bindBaseImage(){
		bindImage(TextureReference.GUI_BASE);
	}
	
	public void bindBGImage(){
		if(bgImage != null)
			bindImage(bgImage);
	}
	
	public final void drawLeft(String s, int x, int y, int color){
		x += 9;
		y += 9;
		fontRenderer.drawString(s, x, y, color);
	}
	
	public final void drawCentered(String s, int xMid, int y, int color){
		drawLeft(s, xMid - fontRenderer.getStringWidth(s) / 2, y, color);
	}
}