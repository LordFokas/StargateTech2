package lordfokas.stargatetech2.core.item;

import lordfokas.stargatetech2.core.block.BlockNaquadah;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockNaquadah extends ItemBlock {
	public ItemBlockNaquadah(Block b) {
		super(b);
		setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack){
		String type = stack.getItemDamage() == BlockNaquadah.ORE ? "ore" : "block";
		return "block.naquadah." + type;
	}
}
