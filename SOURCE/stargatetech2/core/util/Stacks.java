package stargatetech2.core.util;

import java.util.ArrayList;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import stargatetech2.core.api.StackManager;

public class Stacks {
	// Vanilla Stacks
	public static ItemStack bucket, chest, diamond, ironBlock, glass, pearl, quartz, redstone, slab, stick, stone;
	
	// ThermalExpansion Stacks
	public static ItemStack machine, coilGold;
	
	// StargateTech 2 Stacks
	public static ItemStack naqIngot, naqDust, naqPlate, lattice, circuit, coilNaq, coilEnd;
	
	public static void init(){
		//##########################################################################################
		// VANILLA
		bucket = new ItemStack(Item.bucketEmpty);
		chest = new ItemStack(Block.chest);
		diamond = new ItemStack(Item.diamond);
		ironBlock = new ItemStack(Block.blockIron);
		glass = new ItemStack(Block.thinGlass);
		pearl = new ItemStack(Item.enderPearl);
		quartz = new ItemStack(Item.netherQuartz);
		redstone = new ItemStack(Item.redstone);
		slab = new ItemStack(Block.stoneSingleSlab);
		stick = new ItemStack(Item.stick);
		stone = new ItemStack(Block.stone);
		
		//##########################################################################################
		// THERMAl EXPANSION 3
		machine =	fromTE3("machineFrame");
		coilGold =	fromTE3("powerCoilGold");
		
		//##########################################################################################
		// STARGATETECH 2
		naqIngot	= StackManager.instance.get("naquadahIngot");
		naqDust		= StackManager.instance.get("naquadahDust");
		naqPlate	= StackManager.instance.get("naquadahPlate");
		lattice		= StackManager.instance.get("lattice");
		circuit		= StackManager.instance.get("circuitCrystal");
		coilNaq		= StackManager.instance.get("coilNaquadah");
		coilEnd		= StackManager.instance.get("coilEnder");
	}
	
	private static ItemStack fromTE3(String name){
		return GameRegistry.findItemStack("ThermalExpansion", name, 1);
	}
}