package lordfokas.stargatetech2.util;

import lordfokas.stargatetech2.modules.ModuleTransport;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class StargateTab extends CreativeTabs {
	private static final StargateTab INSTANCE = new StargateTab();
	public static StargateTab instance(){ return INSTANCE; }
	
	public StargateTab(){
		super("StargateTech 2");
	}
	
	@Override
	public String getTranslatedTabLabel(){
		return "StargateTech 2";
	}
	
	@Override
	public ItemStack getIconItemStack(){
		return Stacks.coilNaq;
		/*return new ItemStack(ModuleTransport.stargate, 1);*/
	}

	@Override
	public Item getTabIconItem() {
		return null;
	}
}