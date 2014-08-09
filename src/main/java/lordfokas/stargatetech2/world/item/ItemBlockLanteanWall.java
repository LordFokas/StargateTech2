package lordfokas.stargatetech2.world.item;

import lordfokas.stargatetech2.core.reference.ModReference;
import lordfokas.stargatetech2.core.util.Color;
import lordfokas.stargatetech2.world.ModuleWorld;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockLanteanWall extends ItemBlock {

	public ItemBlockLanteanWall(Block b) {
		super(b);
		setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack){
		Color color = ModuleWorld.lanteanWall.getColor(stack.getItemDamage());
		return ModReference.MOD_ID + ":block.lanteanWall_" + color.name.toLowerCase().replace(' ', '_');
	}
}