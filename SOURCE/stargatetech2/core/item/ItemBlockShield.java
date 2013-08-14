package stargatetech2.core.item;

import java.util.List;

import stargatetech2.core.block.BlockShield;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBlockShield extends ItemBlock {

	public ItemBlockShield(int i, Block b) {
		super(b.blockID - 256);
	}
	
	@Override // Avoids placing this block. It would crash the game.
	public boolean placeBlockAt(ItemStack i, EntityPlayer p, World w, int x, int y, int z, int s, float hX, float hY, float hZ, int m){
		return false;
	}
}
