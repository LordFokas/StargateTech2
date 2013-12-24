package stargatetech2.common.util;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import stargatetech2.core.ModuleCore;

public class StargateTab extends CreativeTabs {
	public static final StargateTab instance = new StargateTab();
	public static int iconID;
	
	public StargateTab(){
		super("StargateTech 2");
	}
	
	@Override
	public String getTranslatedTabLabel(){
		return "StargateTech 2";
	}
	
	@Override
	public ItemStack getIconItemStack(){
		return new ItemStack(ModuleCore.stargate, 1);
	}
}
