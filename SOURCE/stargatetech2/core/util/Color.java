package stargatetech2.core.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Color {
	private static int colors = 16;
	public static final Color[] COLORS = new Color[colors];
	
	public static final Color BLACK			= new Color(0x19, 0x19, 0x19, "Black");
	public static final Color RED			= new Color(0x99, 0x33, 0x33, "Red");
	public static final Color GREEN			= new Color(0x66, 0x7F, 0x33, "Green");
	public static final Color BROWN			= new Color(0x66, 0x4C, 0x33, "Brown");
	public static final Color BLUE			= new Color(0x33, 0x4C, 0xB2, "Blue");
	public static final Color PURPLE		= new Color(0x7F, 0x3F, 0xB2, "Purple");
	public static final Color CYAN			= new Color(0x4C, 0x7F, 0x99, "Cyan");
	public static final Color LIGHT_GRAY	= new Color(0x99, 0x99, 0x99, "Light Gray");
	public static final Color DARK_GRAY		= new Color(0x4C, 0x4C, 0x4C, "Dark Gray");
	public static final Color PINK			= new Color(0xF2, 0x7F, 0xA2, "Pink");
	public static final Color LIME			= new Color(0x7F, 0xCC, 0x19, "Lime");
	public static final Color YELLOW		= new Color(0xE5, 0xE5, 0x33, "Yellow");
	public static final Color LIGHT_BLUE	= new Color(0x66, 0x99, 0xD8, "Light Blue");
	public static final Color MAGENTA		= new Color(0xB2, 0x4C, 0xD8, "Magenta");
	public static final Color ORANGE		= new Color(0xD8, 0x7F, 0x33, "Orange");
	public static final Color WHITE			= new Color(0xFF, 0xFF, 0xFF, "White");
	
	public final int id;
	public final int r, g, b;
	public final String name;
	private final ItemStack dye;
	
	public Color(int red, int green, int blue, String name){
		this.id = --colors;
		this.r = red;
		this.g = green;
		this.b = blue;
		this.name = name;
		this.dye = new ItemStack(Item.dyePowder, 1, 15 - id);
		COLORS[id] = this;
	}
	
	public float r(){
		return ((float)r) / 255F;
	}
	
	public float g(){
		return ((float)g) / 255F;
	}
	
	public float b(){
		return ((float)b) / 255F;
	}
	
	public ItemStack getDye(){
		return dye.copy();
	}
}