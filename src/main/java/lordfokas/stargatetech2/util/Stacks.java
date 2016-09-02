package lordfokas.stargatetech2.util;

import lordfokas.stargatetech2.modules.ModuleAutomation;
import lordfokas.stargatetech2.modules.ModuleCore;
import lordfokas.stargatetech2.modules.core.BlockNaquadah;
import lordfokas.stargatetech2.modules.core.ItemNaquadah;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class Stacks {
	// Vanilla Stacks
	public static ItemStack book, bucket, chest, glass, glowDust, glowBlock, pearl, redDust, redBlock, slab, stick, stone;
	
	// ThermalExpansion Stacks
	public static ItemStack machine_0, machine_1, machine_2, signalum, coilGold;
	
	// StargateTech 2 Stacks
	public static ItemStack naqIngot, naqDust, naqPlate, naqOre, naqBlock, lattice, circuit, coilNaq, coilEnd, busCable;
	
	public static void init(){
		//##########################################################################################
		// VANILLA
		book = new ItemStack(Items.BOOK);
		bucket = new ItemStack(Items.BUCKET);
		chest = new ItemStack(Blocks.CHEST);
		glass = new ItemStack(Blocks.GLASS_PANE);
		glowBlock = new ItemStack(Blocks.GLOWSTONE);
		glowDust = new ItemStack(Items.GLOWSTONE_DUST);
		pearl = new ItemStack(Items.ENDER_PEARL);
		redBlock = new ItemStack(Blocks.REDSTONE_BLOCK);
		redDust = new ItemStack(Items.REDSTONE);
		slab = new ItemStack(Blocks.STONE_SLAB);
		stick = new ItemStack(Items.STICK);
		stone = new ItemStack(Blocks.STONE);
		
		//##########################################################################################
		// THERMAl EXPANSION 4
		machine_0 =	fromTE4("frameMachineBasic");
		machine_1 =	fromTE4("frameMachineHardened");
		machine_2 =	fromTE4("frameMachineReinforced");
		signalum = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("ThermalFoundation", "ingotSignalum")), 1);
		coilGold =	fromTE4("powerCoilGold");
		
		//##########################################################################################
		// STARGATETECH 2
		naqOre		= new ItemStack(ModuleCore.naquadahBlock, 1, BlockNaquadah.Type.ORE.ordinal());
		naqBlock	= new ItemStack(ModuleCore.naquadahBlock, 1, BlockNaquadah.Type.BLOCK.ordinal());
		naqIngot	= new ItemStack(ModuleCore.naquadahItem, 1, ItemNaquadah.Type.INGOT.ordinal());
		naqDust		= new ItemStack(ModuleCore.naquadahItem, 1, ItemNaquadah.Type.DUST.ordinal());
		naqPlate	= new ItemStack(ModuleCore.naquadahItem, 1, ItemNaquadah.Type.PLATE.ordinal());
		circuit		= new ItemStack(ModuleCore.naquadahItem, 1, ItemNaquadah.Type.CIRCUIT.ordinal());
		coilNaq		= new ItemStack(ModuleCore.naquadahItem, 1, ItemNaquadah.Type.COIL_NAQ.ordinal());
		coilEnd		= new ItemStack(ModuleCore.naquadahItem, 1, ItemNaquadah.Type.COIL_END.ordinal());
		busCable	= new ItemStack(ModuleAutomation.busCable);
	}
	
	private static ItemStack fromTE4(String name){
		return new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("ThermalExpansion", name)), 1);
	}
}