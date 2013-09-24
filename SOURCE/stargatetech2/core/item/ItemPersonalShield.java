package stargatetech2.core.item;

import net.minecraft.util.DamageSource;
import stargatetech2.common.base.BaseItem;
import stargatetech2.common.reference.ItemReference;

public class ItemPersonalShield extends BaseItem {

	public ItemPersonalShield() {
		super(ItemReference.PERSONAL_SHIELD);
		this.setMaxStackSize(1);
		this.setMaxDamage(1000);
	}
	
	public boolean blocksDamage(DamageSource source){
		return true;
	}
}
