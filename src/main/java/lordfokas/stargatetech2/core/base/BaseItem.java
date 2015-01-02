package lordfokas.stargatetech2.core.base;

import lordfokas.stargatetech2.core.StargateTab;
import lordfokas.stargatetech2.core.reference.ModReference;
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
