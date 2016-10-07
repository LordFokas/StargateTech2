package lordfokas.naquadria.item;

import lordfokas.stargatetech2.reference.ModReference;
import lordfokas.stargatetech2.util.StargateTab;
import net.minecraft.item.Item;

public abstract class BaseItem extends Item {
	public BaseItem(String uName) {
		this.setUnlocalizedName(ModReference.MOD_ID + "." + uName);
		this.setCreativeTab(StargateTab.instance());
	}
}
