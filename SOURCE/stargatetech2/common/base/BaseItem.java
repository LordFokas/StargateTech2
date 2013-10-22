package stargatetech2.common.base;

import net.minecraft.item.Item;
import stargatetech2.StargateTech2;
import stargatetech2.common.reference.ModReference;
import stargatetech2.common.util.StargateTab;

public abstract class BaseItem extends Item {
	public BaseItem(String uName) {
		super(StargateTech2.instance.config.getItemID(uName));
		this.setUnlocalizedName(uName);
		this.setTextureName(ModReference.MOD_ID + ":" + uName);
		this.setCreativeTab(StargateTab.instance);
	}
}
