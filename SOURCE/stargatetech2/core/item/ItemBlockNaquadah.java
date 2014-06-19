package stargatetech2.core.item;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import stargatetech2.core.block.BlockNaquadah;
import stargatetech2.core.reference.ModReference;

public class ItemBlockNaquadah extends ItemBlock {
	public ItemBlockNaquadah(int id) {
		super(id);
		setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack){
		String type = stack.getItemDamage() == BlockNaquadah.ORE ? "ore" : "block";
		return ModReference.MOD_ID + ":block.naquadah." + type;
	}
}
