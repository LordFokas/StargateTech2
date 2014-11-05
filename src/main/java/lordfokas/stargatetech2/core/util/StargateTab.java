package lordfokas.stargatetech2.core.util;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import lordfokas.stargatetech2.transport.ModuleTransport;

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
		return new ItemStack(ModuleTransport.stargate, 1);
	}

	@Override
	public Item getTabIconItem() {
		return null;
	}
}