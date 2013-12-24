package stargatetech2.core.worldgen;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import stargatetech2.core.ModuleCore;
import stargatetech2.core.item.ItemNaquadah;

public class LootGenerator {
	public static final ArrayList<ItemStack> LOOT_COMMON, LOOT_RARE, LOOT_EPIC;
	
	static{
		LOOT_COMMON = new ArrayList<ItemStack>();
		LOOT_COMMON.add(new ItemStack(ModuleCore.naquadah, 16, ItemNaquadah.INGOT.ID));
		LOOT_COMMON.add(new ItemStack(ModuleCore.naquadah, 24, ItemNaquadah.DUST.ID));
		LOOT_COMMON.add(new ItemStack(ModuleCore.naquadah, 8, ItemNaquadah.BAR.ID));
		LOOT_COMMON.add(new ItemStack(ModuleCore.naquadah, 8, ItemNaquadah.PLATE.ID));
		LOOT_COMMON.add(new ItemStack(ModuleCore.naquadah, 12, ItemNaquadah.LATTICE.ID));
		LOOT_COMMON.add(new ItemStack(ModuleCore.naquadah, 6, ItemNaquadah.CIRCUIT.ID));
		LOOT_COMMON.add(new ItemStack(ModuleCore.naquadah, 1, ItemNaquadah.PWCR1.ID));
		LOOT_COMMON.add(new ItemStack(ModuleCore.naquadahOre, 12));
		
		LOOT_RARE = new ArrayList<ItemStack>();
		LOOT_RARE.add(new ItemStack(ModuleCore.naquadahRail, 4));
		LOOT_RARE.add(new ItemStack(ModuleCore.particleIonizer, 1));
		LOOT_RARE.add(new ItemStack(ModuleCore.shieldEmitter, 2));
		LOOT_RARE.add(new ItemStack(ModuleCore.naquadah, 1, ItemNaquadah.PWCR2.ID));
		
		LOOT_EPIC = new ArrayList<ItemStack>();
		LOOT_EPIC.add(new ItemStack(ModuleCore.personalShield, 1));
		LOOT_EPIC.add(new ItemStack(ModuleCore.transportRing, 2));
		LOOT_EPIC.add(new ItemStack(ModuleCore.naquadah, 1, ItemNaquadah.PWCR3.ID));
	}
	
	public enum LootLevel{
		COMMON,
		RARE,
		EPIC
	}
	
	public static void generateLootChest(World w, int x, int y, int z, LootLevel level){
		w.setBlock(x, y, z, Block.chest.blockID);
		populate((TileEntityChest)w.getBlockTileEntity(x, y, z), level);
	}
	
	private static void populate(IInventory inventory, LootLevel level){
		switch(level){
			case EPIC:
				populate(inventory, LOOT_EPIC, 1);
			case RARE:
				populate(inventory, LOOT_RARE, 2);
			case COMMON:
				populate(inventory, LOOT_COMMON, 5);
		}
	}
	
	private static void populate(IInventory inventory, ArrayList<ItemStack> loot, int amount){
		Random r = new Random();
		for(int i = 0; i < amount; i++){
			if(r.nextInt(100) < 75){
				ItemStack listed = loot.get(r.nextInt(loot.size()));
				ItemStack placed = listed.copy();
				placed.stackSize = 1 + r.nextInt(listed.stackSize);
				putStack(inventory, placed, r);
			}
		}
	}
	
	private static void putStack(IInventory inventory, ItemStack stack, Random r){
		int slot;
		do{
			slot = r.nextInt(inventory.getSizeInventory());
		}while(inventory.getStackInSlot(slot) != null);
		inventory.setInventorySlotContents(slot, stack);
	}
}
