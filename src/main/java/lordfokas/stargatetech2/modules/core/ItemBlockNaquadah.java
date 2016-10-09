package lordfokas.stargatetech2.modules.core;

import lordfokas.stargatetech2.util.Helper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockNaquadah extends ItemBlock {
	public ItemBlockNaquadah(BlockNaquadah b) {
		super(b);
		setHasSubtypes(true);
		setRegistryName(b.getRegistryName());
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack){
		String type = stack.getItemDamage() == BlockNaquadah.Type.ORE.ordinal() ? "naquadah_ore" : "naquadah_block";
		return Helper.unlocalize("block", type);
	}
}
