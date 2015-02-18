package lordfokas.stargatetech2.lib.gui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import cofh.lib.gui.element.ElementBase;
import cofh.lib.gui.element.TabBase;

public abstract class BaseTab extends TabBase{
	protected static final int LMB = 0, RMB = 1, MMB = 2;
	private static final int SHADOW = 0x30 << 24;
	private ArrayList<ClickHandler> handlers;
	protected int baseX, baseY;
	private ItemStack icon;
	
	public BaseTab(BaseGUI gui, String name, ItemStack icon, int color) {
		super(gui);
		this.name = name;
		this.icon = icon;
		this.backgroundColor = color;
	}
	
	protected BaseGUI gui(){
		return (BaseGUI) gui;
	}
	
	@Override
	protected void drawTabIcon(String arg0) {
		gui.drawIcon(icon.getIconIndex(), posXOffset(), posY + 3, 1);
	}
	
	@Override
	public void draw() {
		drawBackground();
	}
	
	@Override
	public void addTooltip(List<String> list) {
		if(!isFullyOpened()) list.add(name);
	}
	
	@Override
	public final BaseTab setSize(int x, int y){
		this.maxWidth = x;
		this.maxHeight = y;
		return this;
	}
	
	@Override
	protected void drawBackground() {
		super.drawBackground();
		drawTabIcon("");
		if(!isFullyOpened()) return;
		gui.drawSizedModalRect(baseX, baseY, posXOffset() + maxWidth - 10, posY + maxHeight - 10, SHADOW);
		getFontRenderer().drawStringWithShadow(this.name, posXOffset() + 18, this.posY + 6, this.headerColor);
		GL11.glColor4f(1, 1, 1, 1);
		drawTab();
	}
	
	protected abstract void drawTab();
	
	@Override
	public boolean onMousePressed(int mx, int my, int mouse) {
		if(isFullyOpened()){
			for(ClickHandler handler : handlers){
				if(handler.hits(mx, my)){
					handler.run(mouse);
					return true;
				}
			}
		}else{
			return super.onMousePressed(mx, my, mouse);
		}
		return false;
	}
	
	protected abstract class ClickHandler{
		private int px, py, sx, sy;
		
		public ClickHandler(int px, int py, int sx, int sy){
			this.px = px;
			this.py = py;
			this.sx = sx;
			this.sy = sy;
		}
		
		public boolean hits(int mx, int my){
			return intersects(mx, my, px, py, sx, sy);
		}
		
		private boolean intersects(int mx, int my, int px, int py, int sx, int sy){
			int fx = px + sx, fy = py + sy;
			return mx >= px && mx < fx && my >= py && my < fy;
		}
		
		public abstract void run(int mb);
	}
	
	@Override
	public ElementBase setPosition(int x, int y) {
		super.setPosition(x, y);
		baseX = posXOffset() + 5;
		baseY = posY + 24;
		handlers = new ArrayList();
		addHandlers();
		handlers.add(new ClickHandler(baseX, baseY, maxWidth - 15, maxHeight - 34) {
			@Override public void run(int mb){}
		});
		return this;
	}
	
	public abstract void addHandlers();
	
	public final void addHandler(ClickHandler handler){
		handlers.add(handler);
	}
}
