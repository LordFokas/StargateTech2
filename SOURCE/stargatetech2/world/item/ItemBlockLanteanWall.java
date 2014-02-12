package stargatetech2.world.item;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import stargatetech2.core.reference.ModReference;
import stargatetech2.core.util.Color;
import stargatetech2.world.ModuleWorld;

public class ItemBlockLanteanWall extends ItemBlock {

	public ItemBlockLanteanWall(int id) {
		super(id);
		setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack){
		Color color = ModuleWorld.lanteanWall.getColor(stack.getItemDamage());
		return ModReference.MOD_ID + ":block.lanteanWall_" + color.name.toLowerCase().replace(' ', '_');
	}
}