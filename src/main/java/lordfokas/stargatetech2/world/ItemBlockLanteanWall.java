package lordfokas.stargatetech2.world;

import lordfokas.stargatetech2.ModuleWorld;
import lordfokas.stargatetech2.core.Color;
import lordfokas.stargatetech2.core.reference.ModReference;
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
		return "block.lanteanWall_" + color.name.toLowerCase().replace(' ', '_');
	}
}