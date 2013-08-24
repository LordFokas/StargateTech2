package stargatetech2.core.item;

import stargatetech2.common.base.BaseItem;
import stargatetech2.common.reference.ItemReference;
import stargatetech2.common.util.StargateTab;

public class ItemNaquadahIngot extends BaseItem {

	public ItemNaquadahIngot() {
		super(ItemReference.NAQUADAH_INGOT);
		StargateTab.iconID = itemID;
	}
}