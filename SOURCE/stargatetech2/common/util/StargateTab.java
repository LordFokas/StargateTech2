package stargatetech2.common.util;

import stargatetech2.common.reference.ModReference;
import net.minecraft.creativetab.CreativeTabs;

public class StargateTab extends CreativeTabs {
	public static final StargateTab instance = new StargateTab();
	public static int tabletID;
	
	public StargateTab(){
		super("StargateTech 2");
	}
	
	@Override
	public int getTabIconItemIndex(){
		return tabletID;
	}
	
	@Override
	public String getTranslatedTabLabel(){
		return "StargateTech 2";
	}
}
