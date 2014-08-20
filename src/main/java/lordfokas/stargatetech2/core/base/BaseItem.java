package lordfokas.stargatetech2.core.base;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import lordfokas.stargatetech2.StargateTech2;
import lordfokas.stargatetech2.core.reference.ModReference;
import lordfokas.stargatetech2.core.util.StargateTab;

public abstract class BaseItem extends Item {
	private String unlocalized;
	
	public BaseItem(String uName) {
		unlocalized = uName;
		this.setTextureName(ModReference.MOD_ID + ":" + uName);
		this.setCreativeTab(StargateTab.instance);
	}
	
	/*
	// WHY?
	@Override
	public String getUnlocalizedName(){
		return "item." + unlocalized;
	}
	
	// NO, SRSLY, WHY?
	@Override
	public String getUnlocalizedName(ItemStack stack){
		return "item." + unlocalized;
	}*/
}
