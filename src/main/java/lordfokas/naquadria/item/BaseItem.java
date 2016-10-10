package lordfokas.naquadria.item;

import lordfokas.stargatetech2.reference.ModReference;
import lordfokas.stargatetech2.util.Helper;
import lordfokas.stargatetech2.util.StargateTab;
import net.minecraft.item.Item;

public abstract class BaseItem extends Item {
	public BaseItem(String registryName) {
		this.setRegistryName(registryName);
		this.setUnlocalizedName("stargatetech2." + registryName);
		this.setCreativeTab(StargateTab.instance());
	}
}
