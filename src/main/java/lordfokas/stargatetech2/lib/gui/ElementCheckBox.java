package lordfokas.stargatetech2.lib.gui;

public class ElementCheckBox /*extends ElementButton*/{
	/*private boolean isChecked = false;
	private boolean autoCheck = false;
	private int stringWidth = 0;
	private String label = "";
	private int ex, ey, dx, dy;
	public int color = 0xFF707070;
	
	public ElementCheckBox(GuiBase gui, int px, int py, String action, int ex, int ey, int dx, int dy, int sy, String tex) {
		super(gui, px, py, action, 0, 0, 0, 0, 0, 0, sy, sy, tex);
		this.ex = ex;
		this.ey = ey;
		this.dx = dx;
		this.dy = dy;
	}
	
	@Override
	public void drawBackground(int mx, int my, float partial) {
		GL11.glColor4f(1, 1, 1, 1);
		RenderHelper.bindTexture(texture);
		int tx = isChecked ? ex : dx;
		int ty = isChecked ? ey : dy;
		drawTexturedModalRect(posX, posY, tx, ty, sizeY, sizeY);
		if(this.intersectsWith(mx, my)){
			int px = posX + sizeY + 1;
			int py = posY + sizeY - 2;
			drawModalRect(px, py, px + stringWidth - 1, py + 1, color);
		}
		// because some CoFH elements being rendered afterwards don't reset color.
		GL11.glColor4f(1, 1, 1, 1);
	}
	
	@Override
	public void drawForeground(int mx, int my) {
		getFontRenderer().drawString(label, posX + sizeY + 1, posY + 2, color);
	}
	
	public ElementCheckBox setLabel(String label){
		this.label = label;
		this.stringWidth = getFontRenderer().getStringWidth(label);
		this.sizeX = this.sizeY + 1 + stringWidth;
		return this;
	}
	
	public ElementCheckBox setAuto(boolean auto){
		this.autoCheck = auto;
		return this;
	}
	
	@Override
	public boolean onMousePressed(int x, int y, int mouse) {
		if(isEnabled()){
			gui.handleElementButtonClick(name, !isChecked ? 1 : 0);
			if(autoCheck) isChecked = !isChecked;
			return true;
		}
		return false;
	}
	
	public void setChecked(boolean checked){
		this.isChecked = checked;
	}
	
	public boolean isChecked(){
		return this.isChecked;
	}*/
}