package stargatetech2.core.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import stargatetech2.core.machine.FaceColor;
import stargatetech2.core.reference.TextureReference;

public abstract class BaseGUI extends GuiContainer {
	private int _xoff = 0, _yoff = 0;
	private boolean onBackground;
	private boolean isNativeRender;
	private boolean usesTextHandler = false;
	protected ResourceLocation bgImage = null;
	
	private ArrayList<TabWrapper> tabs = new ArrayList();
	private ArrayList<IGauge> gauges = new ArrayList();
	private ArrayList<HandlerWrapper<IClickHandler>> clickHandlers = new ArrayList();
	private ArrayList<HandlerWrapper<IHoverHandler>> hoverHandlers = new ArrayList();
	
	public static interface ITab{
		public void register(BaseGUI gui);
		public TabColor getColor();
		public ItemStack getIcon();
		public String getName();
		public int getSizeX();
		public int getSizeY();
		public void render();
		public boolean handleClick(int x, int y);
		
		public static enum TabColor{
			RED		(96,  0),
			BLUE	(96, 16),
			YELLOW	(96, 32),
			GREEN	(96, 48);
			
			public final int x, y;
			
			private TabColor(int x, int y){
				this.x = x;
				this.y = y;
			}
		}
	}
	
	public static interface IGauge{
		public void register(BaseGUI gui);
		public void renderGauge();
		public void renderTooltip();
		public void update();
	}
	
	public static interface IClickHandler{
		public void onClick(int x, int y);
	}
	
	public static interface IHoverHandler{
		public void onHover(int x, int y);
		public void onLeave();
	}
	
	private static class TabWrapper{
		public int _hx, _hy;
		public final ITab tab;
		private BaseGUI gui;
		private int size;
		private long lastUpdate = 0;
		private int grow;
		private boolean isHover = false;
		private static final int GROWTH_FACTOR = 25;
		
		public TabWrapper(ITab tab, BaseGUI gui){
			this.tab = tab;
			this.gui = gui;
		}
		
		public void expand(){
			if(isContracted()){
				grow = GROWTH_FACTOR;
			}
		}
		
		public void contract(){
			if(!isContracted()){
				grow = -GROWTH_FACTOR;
			}
		}
		
		public boolean isExpanded(){
			return grow == 0 && size == 100;
		}
		
		public boolean isContracted(){
			return grow == 0 && size == 0;
		}
		
		public void update(){
			long time = Minecraft.getMinecraft().theWorld.getTotalWorldTime();
			if(lastUpdate < time && grow != 0){
				size += grow;
				if(size > 100) size = 100;
				if(size < 0  ) size = 0;
				if(size == 100 || size == 0){
					grow = 0;
				}
			lastUpdate = time;
			}
		}
		
		public int getX(int off){
			if(size == 0){
				return 0;
			}else{
				return (tab.getSizeX() + off) * size / 100;
			}
		}
		
		public int getY(int off){
			if(size == 0){
				return 0;
			}else{
				return (tab.getSizeY() + off) * size / 100;
			}
		}
		
		public void setHover(boolean hover){
			isHover = hover;
		}
		
		public boolean isHover(){
			return isHover;
		}
	}
	
	private static class HandlerWrapper<H>{
		public final H handler;
		public int minX, minY, maxX, maxY;
		
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
	
	protected final void addClickHandler(IClickHandler handler, int x, int y, int xS, int yS){
		clickHandlers.add(new HandlerWrapper<IClickHandler>(handler, x, y, x+xS, y+yS));
	}
	
	protected final void addHoverHandler(IHoverHandler handler, int x, int y, int xS, int yS){
		hoverHandlers.add(new HandlerWrapper<IHoverHandler>(handler, x, y, x+xS, y+yS));
	}
	
	protected final void addGauge(IGauge gauge){
		gauge.register(this);
		gauges.add(gauge);
	}
	
	protected final void addTab(ITab tab){
		tab.register(this);
		tabs.add(new TabWrapper(tab, this));
	}
	
	protected final TabWrapper getTab(int x, int y){
		int off = 15;
		for(TabWrapper tab : tabs){
			int tx = tab.getX(-16) + 22;
			int ty = tab.getY(-16) + 22;
			if(off <= y && y < (off + ty)){
				if(x > -tx-5 && x <= -5){
					return tab;
				}
			}
			off += ty;
		}
		return null;
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
			TabWrapper hit = getTab(x, y);
			if(hit != null){
				int yoff = 15;
				for(TabWrapper tab : tabs){
					if(tab == hit){
						if(hit.isContracted()) hit.expand();
						else if(hit.tab.handleClick(x + 26 + tab.getX(-16), y - yoff)){
							hit.contract();
						}
					}else{
						yoff += tab.getY(-16) + 22;
						tab.contract();
					}
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
			TabWrapper hit = getTab(x, y);
			for(TabWrapper tab : tabs){
				tab.setHover(tab == hit);
				if(tab.isHover()){
					tab._hx = x;
					tab._hy = y;
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
	
	private void updateTabs(){
		for(TabWrapper wrapper : tabs){
			wrapper.update();
		}
	}
	
	@Override
	protected final void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		updateTabs();
		onBackground = true;
		if(bgImage != null){
			bindImage(bgImage);
			int x = xSize - 18;
			int y = ySize - 18;
			drawLocalQuad(0, 0, 0, x, 0, y, x, y);
		}
		bindBaseImage();
		drawTabBackgrounds();
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
		drawTabForegrounds();
		drawForeground();
		for(IGauge gauge : gauges){
			gauge.renderTooltip();
		}
		for(TabWrapper tab : tabs){
			if(tab.isHover() && tab.isContracted()){
				drawHover(tab._hx, tab._hy+9, tab.tab.getName());
			}
		}
	}
	
	private void drawTabBackgrounds(){
		int off = 0;
		for(int t = 0; t < tabs.size(); t++){
			TabWrapper wrapper = tabs.get(t);
			int tx = wrapper.tab.getColor().x;
			int ty = wrapper.tab.getColor().y;
			int sx = 19 + wrapper.getX(-16);
			int sy = 16 + wrapper.getY(-16);
			drawLocalQuad(-7-sx, off + 15, tx, tx + 3, ty, ty + 3, 3, 3);
			drawLocalQuad(-7-sx, off + 18+sy, tx, tx + 3, ty + 13, ty + 16, 3, 3);
			drawLocalQuad(-7-sx, off + 18, tx, tx + 3, ty + 3, ty + 13, 3, sy);
			drawLocalQuad(-4-sx, off + 15, tx + 3, tx + 13, ty, ty + 3, sx, 3);
			drawLocalQuad(-4-sx, off + 18+sy, tx + 3, tx + 13, ty + 13, ty + 16, sx, 3);
			drawLocalQuad(-4-sx, off + 18, tx + 3, tx + 13, ty + 3, ty + 13, sx, sy);
			GL11.glDisable(GL11.GL_LIGHTING); // TODO: get rid of this workaround AFTER mc stops fucking up OGL state.
			itemRenderer.renderItemAndEffectIntoGUI(fontRenderer, mc.renderEngine, wrapper.tab.getIcon(), guiLeft-sx+5, guiTop+27+off);
			if(wrapper.isExpanded()){
				GL11.glDisable(GL11.GL_LIGHTING); // TODO: get rid of this workaround AFTER mc stops fucking up OGL state.
				drawLeft(wrapper.tab.getName(), guiLeft-sx+15, guiTop+23+off, 0xFFFFFF);
			}
			bindBaseImage();
			off += sy + 6;
		}
	}
	
	private void drawTabForegrounds(){
		int yoff = 15;
		int xoff = 0;
		for(TabWrapper wrapper : tabs){
			int sy = 22 + wrapper.getY(-16);
			int sx = 22 + wrapper.getX(-16);
			xoff = -sx-4;
			if(wrapper.isExpanded()){
				_xoff = xoff;
				_yoff = yoff;
				wrapper.tab.render();
			}
			yoff += sy;
		}
		_xoff = 0;
		_yoff = 0;
	}
	
	private void updateGauges(){
		for(IGauge gauge : gauges){
			gauge.update();
		}
	}
	
	protected void drawBackground(){}
	protected void drawForeground(){}
	
	public final void drawIcon(float x, float y, Icon icon, ResourceLocation map, int size){
		mc.renderEngine.bindTexture(map);
		drawQuad(x, y, icon.getMinU(), icon.getMaxU(), icon.getMinV(), icon.getMaxV(), size, size);
	}
	
	public final void drawLocalQuad(float x, float y, float xMin, float xMax, float yMin, float yMax, float xStep, float yStep){
		drawQuad(x, y, xMin / 256F, xMax / 256F, yMin / 256F, yMax / 256F, xStep, yStep);
	}
	
	public final void drawQuad(float x, float y, float xMin, float xMax, float yMin, float yMax, float xStep, float yStep){
		x += _xoff;
		y += _yoff;
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
	
	protected final void bindImage(ResourceLocation rl){
		mc.renderEngine.bindTexture(rl);
	}
	
	public final void bindBaseImage(){
		bindImage(TextureReference.GUI_BASE);
	}
	
	public final void bindBGImage(){
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
	
	public final void drawHover(int x, int y, String ... lines){
		drawHover(Arrays.asList(lines), x, y);
	}
	
	public final void drawHover(List<String> lines, int x, int y){
		drawHoveringText(lines, x, y, fontRenderer);
	}
	
	public final void drawFrame(FaceColor color, int xPos, int yPos, int xSize, int ySize){
		bindImage(TextureReference.getTexture("blocks/" + color.getTexture() + ".png"));
		float x0 = 5F / 16F, x1 = 6F / 16F, y0 = 4F / 16F, y1 = 5F / 16F;
		this.drawQuad(xPos, yPos, x0, x1, y0, y1, xSize, 1);
		this.drawQuad(xPos, yPos + ySize - 1, x0, x1, y0, y1, xSize, 1);
		this.drawQuad(xPos, yPos + 1, x0, x1, y0, y1, 1, ySize - 2);
		this.drawQuad(xPos + xSize - 1, yPos + 1, x0, x1, y0, y1, 1, ySize - 2);
	}
	
	public final void playClick(){
		playClick(0.75F);
	}
	
	public final void playClick(float pitch){
		super.mc.sndManager.playSoundFX("random.click", 1.0F, pitch);
	}
}