package lordfokas.naquadria.tileentity.component.inventory;

import lordfokas.naquadria.tileentity.component.IFilter;
import net.minecraft.item.ItemStack;

public class ItemFilter implements IFilter<ItemStack>{
	protected ItemStack filter;
	
	public ItemFilter(ItemStack filter){
		this.filter = filter;
	}
	
	@Override
	public boolean matches(ItemStack object) {
		return ItemStack.areItemsEqual(filter, object);
	}
}
