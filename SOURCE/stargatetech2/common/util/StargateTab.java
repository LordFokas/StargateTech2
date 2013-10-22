package stargatetech2.common.util;

import stargatetech2.common.reference.ModReference;
import stargatetech2.core.ModuleCore;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class StargateTab extends CreativeTabs {
	public static final StargateTab instance = new StargateTab();
	public static int iconID;
	
	public StargateTab(){
		super("StargateTech 2");
	}
	
	/*@Override
	public int getTabIconItemIndex(){
		return iconID;
	}*/
	
	@Override
	public String getTranslatedTabLabel(){
		return "StargateTech 2";
	}
	
	@Override
	public ItemStack getIconItemStack(){
		return new ItemStack(ModuleCore.naquadah, 1, 0);
	}
}
