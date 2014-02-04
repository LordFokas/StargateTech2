package stargatetech2.core.base;

import net.minecraft.item.Item;
import stargatetech2.StargateTech2;
import stargatetech2.core.reference.ModReference;
import stargatetech2.core.util.StargateTab;

public abstract class BaseItem extends Item {
	public BaseItem(String uName) {
		super(StargateTech2.config.getItemID(uName));
		this.setUnlocalizedName(uName);
		this.setTextureName(ModReference.MOD_ID + ":" + uName);
		this.setCreativeTab(StargateTab.instance);
	}
}
