package lordfokas.stargatetech2.lib.gui;

public class ElementTextBox /*extends ElementBase*/{
	/*public int backgroundColor = 0xFF000000;
	public int borderColor = 0xFF808080;
	public int textColor = 0xFFFFFF;
	
	private String filter;
	private int action_m;
	private String action_n;
	private int charCount = 0;
	private int charLimit = 0;
	private StringBuilder input;
	private String str;
	
	public ElementTextBox(BaseGUI gui, int x, int y, int w, int h) {
		super(gui, x, y, w, h);
		input = new StringBuilder();
		str = "";
	}
	
	public String getText(){
		return str;
	}
	
	public void clear(){
		charCount = 0;
		str = "";
		input = new StringBuilder();
	}
	
	@Override
	public void drawBackground(int x, int y, float partial) {
		drawModalRect(posX - 1, posY - 1, posX + sizeX + 1, posY + sizeY + 1, borderColor);
		drawModalRect(posX, posY, posX + sizeX, posY + sizeY, backgroundColor);
	}

	@Override
	public void drawForeground(int x, int y) {
		String print = str;
		FontRenderer fr = getFontRenderer();
		while(fr.getStringWidth(print) > sizeX - 4){
			print = print.substring(1);
		}
		fr.drawStringWithShadow(print, posX + 2, posY + 3, textColor);
	}
	
	public ElementTextBox setAction(String action){
		return setAction(action, -1);
	}
	
	public ElementTextBox setAction(String action, int mouse){
		this.action_n = action;
		this.action_m = mouse;
		return this;
	}
	
	public ElementTextBox setFilter(int maxSize){
		return setFilter(maxSize, null);
	}
	
	public ElementTextBox setFilter(int maxSize, String filter){
		this.charLimit = maxSize;
		this.filter = filter == null ? null : filter.toLowerCase();
		return this;
	}
	
	@Override
	public boolean onKeyTyped(char c, int k) {
		if(k == 14){
			if(charCount > 0){
				input.deleteCharAt(--charCount);
				str = input.toString();
				return true;
			}
			return false;
		}else if((k == 28 || k == 156) && action_n != null){
			((BaseGUI)gui).triggerElementButtonClick(action_n, action_m);
			return true;
		}
		if(charCount >= charLimit) return false;
		String s = new String(new char[]{c}).toLowerCase();
		if(filter == null || filter.contains(s)){
			input.append(c);
			charCount++;
			str = input.toString();
			return true;
		}
		return false;
	}*/
}
