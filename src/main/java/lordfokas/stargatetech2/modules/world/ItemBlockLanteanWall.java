package lordfokas.stargatetech2.modules.world;

import lordfokas.naquadria.render.Color;
import lordfokas.stargatetech2.modules.ModuleWorld;
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