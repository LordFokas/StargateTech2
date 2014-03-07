package stargatetech2.core.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import stargatetech2.automation.ModuleAutomation;
import stargatetech2.core.api.StackManager;
import cpw.mods.fml.common.registry.GameRegistry;

public class Stacks {
	// Vanilla Stacks
	public static ItemStack book, bucket, chest, glass, glowDust, glowBlock, pearl, redDust, redBlock, slab, stick, stone;
	
	// ThermalExpansion Stacks
	public static ItemStack machine, coilGold;
	
	// StargateTech 2 Stacks
	public static ItemStack naqIngot, naqDust, naqPlate, lattice, circuit, coilNaq, coilEnd, busCable;
	
	public static void init(){
		//##########################################################################################
		// VANILLA
		book = new ItemStack(Item.book);
		bucket = new ItemStack(Item.bucketEmpty);
		chest = new ItemStack(Block.chest);
		glass = new ItemStack(Block.thinGlass);
		glowBlock = new ItemStack(Block.glowStone);
		glowDust = new ItemStack(Item.glowstone);
		pearl = new ItemStack(Item.enderPearl);
		redBlock = new ItemStack(Block.blockRedstone);
		redDust = new ItemStack(Item.redstone);
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
		busCable	= new ItemStack(ModuleAutomation.busCable);
	}
	
	private static ItemStack fromTE3(String name){
		return GameRegistry.findItemStack("ThermalExpansion", name, 1);
	}
}