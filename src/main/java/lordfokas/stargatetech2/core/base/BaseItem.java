package lordfokas.stargatetech2.core.base;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import lordfokas.stargatetech2.StargateTech2;
import lordfokas.stargatetech2.core.reference.ModReference;
import lordfokas.stargatetech2.core.util.StargateTab;

public abstract class BaseItem extends Item {
	private String unlocalized;
	
	public BaseItem(String uName) {
		// super(StargateTech2.config.getItemID(uName));
		unlocalized = uName;
		this.setTextureName(ModReference.MOD_ID + ":" + uName);
		this.setCreativeTab(StargateTab.instance);
	}
	
	@Override
	public String getUnlocalizedName(){
		return ModReference.MOD_ID + ":item." + unlocalized;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack){
		return ModReference.MOD_ID + ":item." + unlocalized;
	}
}
