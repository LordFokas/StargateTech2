package lordfokas.stargatetech2.core.base;

import net.minecraft.item.ItemStack;
import lordfokas.stargatetech2.core.base.BaseGUI.ITab;

public abstract class BaseTab implements ITab{
	private final TabColor color;
	private final ItemStack icon;
	private final String name;
	protected BaseGUI gui;
	
	public BaseTab(String name, ItemStack icon, TabColor color){
		this.name = name;
		this.icon = icon;
		this.color = color;
	}
	
	@Override
	public final void register(BaseGUI gui) {
		this.gui = gui;
	}
	
	@Override
	public TabColor getColor() {
		return color;
	}

	@Override
	public ItemStack getIcon() {
		return icon;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public int getSizeX() {
		return 100;
	}
	
	@Override
	public boolean handleClick(int x, int y){
		return true;
	}
	
	protected boolean elementHit(int ex, int ey, int cx, int cy, int sx, int sy){
		return cx >= ex && cx < ex + sx && cy >= ey && cy < ey + sy;
	}
}