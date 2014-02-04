package stargatetech2.core.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import stargatetech2.core.api.StackManager;

public class Stacks {
	// Vanilla Stacks
	public static ItemStack bucket, cauldron, diamond, ironBlock, glass, pearl, quartz, redstone, stick, stone;
	
	// StargateTech 2 Stacks
	public static ItemStack naqIngot, naqDust, naqBar, naqPlate, lattice, circuit, crystal1, crystal2, crystal3;
	
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
		
		naqIngot	= StackManager.instance.get("naquadahIngot");
		naqDust		= StackManager.instance.get("naquadahDust");
		naqBar		= StackManager.instance.get("naquadahBar");
		naqPlate	= StackManager.instance.get("naquadahPlate");
		lattice		= StackManager.instance.get("lattice");
		circuit		= StackManager.instance.get("circuitCrystal");
		crystal1	= StackManager.instance.get("naquadahPowerCrystal_1");
		crystal2	= StackManager.instance.get("naquadahPowerCrystal_2");
		crystal3	= StackManager.instance.get("naquadahPowerCrystal_3");
	}
}