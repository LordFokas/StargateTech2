package stargatetech2.core.util;

import stargatetech2.core.item.ItemNaquadah;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Stacks {
	public static ItemStack bucket, cauldron, diamond, ironBlock, glass, pearl, quartz, redstone, stick, stone;
	
	public static void init(){
		bucket = new ItemStack(Item.bucketEmpty);
		cauldron = new ItemStack(Item.cauldron);
		diamond = new ItemStack(Item.diamond);
		ironBlock = new ItemStack(Block.blockIron);
		glass = new ItemStack(Block.thinGlass);
		pearl = new ItemStack(Item.enderPearl);
		quartz = new ItemStack(Item.netherQuartz);
		redstone = new ItemStack(Item.redstone);
		stick = new ItemStack(Item.stick);
		stone = new ItemStack(Block.stone);
	}
}