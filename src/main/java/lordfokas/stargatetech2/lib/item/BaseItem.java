package lordfokas.stargatetech2.lib.item;

import lordfokas.stargatetech2.reference.ModReference;
import lordfokas.stargatetech2.util.StargateTab;
import net.minecraft.item.Item;

public abstract class BaseItem extends Item {
	private String unlocalized;
	
	public BaseItem(String uName) {
		unlocalized = uName;
		this.setUnlocalizedName(uName);
		this.setTextureName(ModReference.MOD_ID + ":" + uName);
		this.setCreativeTab(StargateTab.instance);
	}
}