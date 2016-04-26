package lordfokas.stargatetech2.lib.item;

import lordfokas.stargatetech2.util.StargateTab;
import net.minecraft.item.Item;

public abstract class BaseItem extends Item {
	public BaseItem(String uName) {
		this.setUnlocalizedName(uName);
		this.setCreativeTab(StargateTab.instance);
	}
}
