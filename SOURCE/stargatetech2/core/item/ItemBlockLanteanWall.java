package stargatetech2.core.item;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import stargatetech2.common.util.Color;
import stargatetech2.core.ModuleCore;

public class ItemBlockLanteanWall extends ItemBlock {

	public ItemBlockLanteanWall(int id) {
		super(id);
		setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack){
		Color color = ModuleCore.lanteanWall.getColor(stack.getItemDamage());
		return ModuleCore.lanteanWall.getUnlocalizedName() + "_" + color.name.toLowerCase();
	}
}