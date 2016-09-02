package lordfokas.stargatetech2.modules.core;

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
		String type = stack.getItemDamage() == BlockNaquadah.Type.ORE.ordinal() ? "ore" : "block";
		return "block.naquadah." + type;
	}
}
