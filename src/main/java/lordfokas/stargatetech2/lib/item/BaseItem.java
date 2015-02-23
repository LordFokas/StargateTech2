package lordfokas.stargatetech2.lib.item;

import lordfokas.stargatetech2.lib.util.NoDefaultTexture;
import lordfokas.stargatetech2.reference.ModReference;
import lordfokas.stargatetech2.util.StargateTab;
import net.minecraft.item.Item;

public abstract class BaseItem extends Item {
	public BaseItem(String uName) {
		this.setUnlocalizedName(uName);
		this.setTextureName(ModReference.MOD_ID + ":" + (getClass().getAnnotation(NoDefaultTexture.class) == null ? uName : "dummy"));
		this.setCreativeTab(StargateTab.instance);
	}
}
