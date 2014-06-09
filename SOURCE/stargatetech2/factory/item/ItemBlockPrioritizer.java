package stargatetech2.factory.item;

import stargatetech2.core.reference.ModReference;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockPrioritizer extends ItemBlock {

	public ItemBlockPrioritizer(int id) {
		super(id);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack){
		String type = "ERROR";
		switch(stack.getItemDamage()){
			case 0: type = "energy"; break;
			case 1: type = "fluid"; break;
			case 2: type = "items"; break;
		}
		return ModReference.MOD_ID + ":block.prioritizer." + type;
	}
}
