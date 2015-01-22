package lordfokas.stargatetech2.abstraction.gui;

import lordfokas.stargatetech2.core.reference.TextureReference;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cofh.core.gui.GuiBaseAdv;
import cofh.lib.gui.element.ElementBase;
import cofh.lib.gui.element.ElementButton;

public class BaseGUI extends GuiBaseAdv{
	private static final int BORDER_THICK = 9;
	private static final int BORDER_WIDE = 25;
	private static final int BORDER_RES = 4;
	private static final int BORDER_RES_OFF = BORDER_THICK - BORDER_RES;
	private static final int BORDER_OFF = BORDER_RES_OFF - BORDER_WIDE;
	protected String title;
	
	public BaseGUI(Container container, int x, int y, ResourceLocation background) {
		super(container, background);
		drawInventory = false;
		drawTitle = false;
		xSize = x + 8;
		ySize = y + 8;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float arg0, int arg1, int arg2) {
		super.drawGuiContainerBackgroundLayer(arg0, arg1, arg2);
		GL11.glColor3f(1, 1, 1);
		bindTexture(TextureReference.GUI_BASE);
		
		drawSizedTexturedModalRect(guiLeft - BORDER_RES_OFF, guiTop - BORDER_RES_OFF, 0, 0, BORDER_WIDE, BORDER_WIDE, 64, 64);
		drawSizedTexturedModalRect(guiLeft + xSize + BORDER_OFF, guiTop - BORDER_RES_OFF, 39, 0, BORDER_WIDE, BORDER_WIDE, 64, 64);
		drawSizedTexturedModalRect(guiLeft - BORDER_RES_OFF, guiTop + ySize + BORDER_OFF, 0, 39, BORDER_WIDE, BORDER_WIDE, 64, 64);
		drawSizedTexturedModalRect(guiLeft + xSize + BORDER_OFF, guiTop + ySize + BORDER_OFF, 39, 39, BORDER_WIDE, BORDER_WIDE, 64, 64);
		
		int height = ySize + 2*BORDER_OFF;
		int width = xSize + 2*BORDER_OFF;
		int ty = height * 4;
		int tx = width * 4;
		int sy = ty * 3 / 8;
		int sx = tx * 3 / 8;
		
		drawSizedTexturedModalRect(guiLeft - BORDER_OFF, guiTop - BORDER_RES_OFF, sx, 0, width, BORDER_THICK, tx, 64);
		drawSizedTexturedModalRect(guiLeft - BORDER_OFF, guiTop + ySize - BORDER_RES, sx, 55, width, BORDER_THICK, tx, 64);
		drawSizedTexturedModalRect(guiLeft - BORDER_RES_OFF, guiTop - BORDER_OFF, 0, sy, BORDER_THICK, height, 64, ty);
		drawSizedTexturedModalRect(guiLeft + xSize - BORDER_RES, guiTop - BORDER_OFF, 55, sy, BORDER_THICK, height, 64, ty);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int arg0, int arg1) {
		super.drawGuiContainerForegroundLayer(arg0, arg1);
		this.drawString(fontRendererObj, title, 8, 8, 0xFFFFFF);
	}
	
	public void triggerElementButtonClick(String button, int mouse) {
		for(ElementBase element : elements){
			if(element instanceof ElementButton){
				String action = ((ElementButton)element).getName();
				if(button.equals(action)){
					if(element.isEnabled()) handleElementButtonClick(action, mouse);
					return;
				}
			}
		}
	}
	
	public void playTonedClick(boolean positive){
		playSound("random.click", 1F, positive ? 0.8F : 0.6F);
	}
}
