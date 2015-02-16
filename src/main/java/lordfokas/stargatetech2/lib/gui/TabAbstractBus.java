package lordfokas.stargatetech2.lib.gui;

import java.util.LinkedList;
import java.util.List;

import lordfokas.stargatetech2.modules.automation.AddressHelper;
import lordfokas.stargatetech2.modules.automation.ISyncBusDevice;
import lordfokas.stargatetech2.modules.automation.PacketUpdateBusAddress;
import lordfokas.stargatetech2.modules.automation.PacketUpdateBusEnabled;
import lordfokas.stargatetech2.reference.TextureReference;
import lordfokas.stargatetech2.util.Stacks;

import org.lwjgl.opengl.GL11;

import cofh.lib.gui.element.ElementBase;
import cofh.lib.gui.element.TabBase;
import cofh.lib.render.RenderHelper;

public class TabAbstractBus extends TabBase{
	private ISyncBusDevice device;
	private LinkedList<ClickHandler> handlers;
	private int selx, sely, togx, togy;
	
	public TabAbstractBus(BaseGUI gui, int side, ISyncBusDevice device) {
		super(gui, side);
		this.device = device;
		this.backgroundColor = 0xFF0088FF;
		this.name = "Abstract Bus";
		this.maxHeight = 100;
		this.maxWidth = 110;
	}
	
	@Override
	public ElementBase setPosition(int arg0, int arg1) {
		super.setPosition(arg0, arg1);
		togx = posXOffset() + 18;
		togy = posY + 30;
		selx = posXOffset() + 18;
		sely = posY + 50;
		
		handlers = new LinkedList();
		handlers.add(new ClickHandler(togx, togy, 24, 12){
			@Override
			public void run() {
				toggleEnabled();
			}
		});
		
		handlers.add(new BitClickHandler(selx + 01, sely + 00, +4));
		handlers.add(new BitClickHandler(selx + 01, sely + 21, -4));
		handlers.add(new BitClickHandler(selx + 13, sely + 00, +3));
		handlers.add(new BitClickHandler(selx + 13, sely + 21, -3));
		handlers.add(new BitClickHandler(selx + 25, sely + 00, +2));
		handlers.add(new BitClickHandler(selx + 25, sely + 21, -2));
		handlers.add(new BitClickHandler(selx + 37, sely + 00, +1));
		handlers.add(new BitClickHandler(selx + 37, sely + 21, -1));
		
		handlers.add(new ClickHandler(posXOffset() + 13, posY + 25, 75, 60) {
			@Override
			public void run(){}
		});
		
		return this;
	}
	
	@Override
	protected void drawTabIcon(String arg0) {
		gui.drawIcon(Stacks.circuit.getIconIndex(), posXOffset(), posY + 3, 1);
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
	protected void drawBackground() {
		super.drawBackground();
		drawTabIcon("");
		if(!isFullyOpened()) return;
		gui.drawSizedModalRect(posXOffset() + 13, posY + 25, posXOffset() + 88, posY + 85, 0xFF0055CC);
		GL11.glColor4f(1, 1, 1, 1);
		RenderHelper.bindTexture(TextureReference.GUI_ELEM_ABUS_ADDR);
		gui.drawSizedTexturedModalRect(selx, sely, 0, 0, 48, 28, 48, 28);
		RenderHelper.bindTexture(TextureReference.GUI_ELEM_TOGGLE);
		gui.drawSizedTexturedModalRect(posXOffset() + 18, posY + 30, 0, device.getEnabled() ? 0 : 12, 24, 12, 24, 24);
		getFontRenderer().drawStringWithShadow(device.getEnabled() ? "Enabled" : "Disabled", posXOffset() + 43, this.posY + 32, -1);
		getFontRenderer().drawStringWithShadow(this.name, posXOffset() + 18, this.posY + 6, this.headerColor);
		String addr = AddressHelper.convert(device.getAddress());
		int px = posXOffset() + 21;
		int py = posY + 61;
		for(int i = 0; i < 4; i++){
			getFontRenderer().drawString(addr.substring(i, i + 1), px + (12 * i), py, -1);
		}
	}
	
	@Override
	public boolean onMousePressed(int mx, int my, int mouse) {
		for(ClickHandler handler : handlers){
			if(handler.hits(mx, my)){
				handler.run();
				return true;
			}
		}
		return false;
	}
	
	private void toggleEnabled(){
		PacketUpdateBusEnabled pube = new PacketUpdateBusEnabled();
		pube.x = device.getXCoord();
		pube.y = device.getYCoord();
		pube.z = device.getZCoord();
		pube.enabled = !device.getEnabled();
		pube.sendToServer();
		((BaseGUI)gui).playTonedClick(pube.enabled);
	}
	
	private void handleBit(int value){
		// Caution: the following code contains bitwise magic
		int shift = 4 * (Math.abs(value) - 1);
		value /= Math.abs(value);
		int mask = 0xF << shift;
		short address = device.getAddress();
		short part = (short) ((address & mask) >> shift);
		part += value;
		if(part < 0) part += 0x10;
		else if(part > 0xF) part -= 0x10;
		part = (short)(part << shift);
		address = (short)((address & ~mask) | part);
		
		PacketUpdateBusAddress puba = new PacketUpdateBusAddress();
		puba.x = device.getXCoord();
		puba.y = device.getYCoord();
		puba.z = device.getZCoord();
		puba.address = (short) address;
		puba.sendToServer();
		
		((BaseGUI)gui).playTonedClick(value > 0);
	}
	
	private abstract class ClickHandler{
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
		
		public abstract void run();
	}
	
	private final class BitClickHandler extends ClickHandler{
		private int bit;
		
		public BitClickHandler(int px, int py, int bit) {
			super(px, py, 10, 7);
			this.bit = bit;
		}

		@Override
		public void run() {
			handleBit(bit);
		}
	}
}
