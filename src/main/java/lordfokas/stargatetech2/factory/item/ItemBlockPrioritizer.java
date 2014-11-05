package lordfokas.stargatetech2.factory.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import lordfokas.stargatetech2.core.reference.ModReference;
import lordfokas.stargatetech2.factory.tileentity.TilePrioritizer;

public class ItemBlockPrioritizer extends ItemBlock {

	public ItemBlockPrioritizer(Block b) {
		super(b);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack){
		String type = "ERROR";
		switch(stack.getItemDamage()){
			case 0: type = "energy"; break;
			case 1: type = "fluid"; break;
			case 2: type = "items"; break;
		}
		return ModReference.MOD_ID + ":block.prioritizer." + type;
	}
	
	@Override
	public boolean placeBlockAt(ItemStack i, EntityPlayer p, World w, int x, int y, int z, int s, float hX, float hY, float hZ, int m){
		boolean placed = super.placeBlockAt(i, p, w, x, y, z, s, hX, hY, hZ, m);
		if(placed){
			TileEntity te = w.getTileEntity(x, y, z);
			if(te instanceof TilePrioritizer){
				((TilePrioritizer)te).setBufferType(i.getItemDamage());
			}
		}
		return placed;	
	}
}
