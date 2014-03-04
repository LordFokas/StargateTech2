package stargatetech2.core.base;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import stargatetech2.core.base.BaseGUI.ITab;
import stargatetech2.core.reference.TextureReference;
import stargatetech2.core.util.IconRegistry;
import stargatetech2.core.util.Stacks;

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
	
	public static class TabAbstractBus extends BaseTab{
		public TabAbstractBus() {
			super("Abstract Bus", Stacks.busCable, TabColor.BLUE);
		}

		@Override
		public int getSizeY() {
			return 60;
		}

		@Override
		public void render() {
			// TODO Auto-generated method stub
		}
	}
	
	public static class TabConfiguration extends BaseTab{
		private final Icon side, top, bottom, side2, top2, bottom2;
		private final Icon blue, green, purple, red, yellow, orange;
		private final int xoff = 20, yoff = 5;
		
		public TabConfiguration() {
			super("Configuration", Stacks.circuit, TabColor.RED);
			side = IconRegistry.blockIcons.get(TextureReference.MACHINE_SIDE);
			top = IconRegistry.blockIcons.get(TextureReference.MACHINE_TOP);
			bottom = IconRegistry.blockIcons.get(TextureReference.MACHINE_BOTTOM);
			side2 = IconRegistry.blockIcons.get(TextureReference.MACHINE_SIDE_I);
			top2 = IconRegistry.blockIcons.get(TextureReference.MACHINE_TOP_I);
			bottom2 = IconRegistry.blockIcons.get(TextureReference.MACHINE_BOTTOM_I);
			blue = IconRegistry.blockIcons.get(TextureReference.INTERFACE_BLUE);
			green = IconRegistry.blockIcons.get(TextureReference.INTERFACE_GREEN);
			purple = IconRegistry.blockIcons.get(TextureReference.INTERFACE_PURPLE);
			red = IconRegistry.blockIcons.get(TextureReference.INTERFACE_RED);
			yellow = IconRegistry.blockIcons.get(TextureReference.INTERFACE_YELLOW);
			orange = IconRegistry.blockIcons.get(TextureReference.INTERFACE_ORANGE);
		}

		@Override
		public int getSizeY() {
			return 80;
		}

		@Override
		public void render() {
			gui.drawIcon(xoff + 23, yoff + 22, top, TextureMap.locationBlocksTexture, 16);
			
			gui.drawIcon(xoff +  5, yoff + 40, side, TextureMap.locationBlocksTexture, 16);
			gui.drawIcon(xoff + 23, yoff + 40, side, TextureMap.locationBlocksTexture, 16);
			gui.drawIcon(xoff + 41, yoff + 40, side, TextureMap.locationBlocksTexture, 16);
			
			gui.drawIcon(xoff + 23, yoff + 58, bottom, TextureMap.locationBlocksTexture, 16);
			gui.drawIcon(xoff + 41, yoff + 58, side, TextureMap.locationBlocksTexture, 16);
		}
	}
}