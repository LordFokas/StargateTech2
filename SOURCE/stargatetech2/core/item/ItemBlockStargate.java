package stargatetech2.core.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import stargatetech2.common.util.Helper;
import stargatetech2.core.network.stargate.StargateNetwork;
import stargatetech2.core.tileentity.TileStargate;
import stargatetech2.core.worldgen.lists.StargateBuildList;

public class ItemBlockStargate extends ItemBlock {
	public static ItemBlockStargate instance;
	private static final int[] ROTATIONS = new int[]{4, 2, 5, 3};
	
	public ItemBlockStargate(int id) {
		super(id);
		instance = this;
	}
	
	@Override
	public boolean placeBlockAt(ItemStack i, EntityPlayer p, World w, int x, int y, int z, int s, float hX, float hY, float hZ, int m){
		if(!StargateNetwork.instance().canPlaceStargateAt(w, x, y, z)){
			return false;
		}
		boolean blockWasPlaced;
		ForgeDirection dir = Helper.yaw2dir(p.rotationYaw);
		StargateBuildList builder = null;
		int xm = dir.offsetZ * dir.offsetZ;
		boolean isX = (xm == 1);
		if(isX){
			builder = StargateBuildList.SGX;
		}else{
			builder = StargateBuildList.SGZ;
		}
		if(!builder.checkHasSpace(w, x, y, z)){
			return false;
		}
		blockWasPlaced = super.placeBlockAt(i, p, w, x, y, z, s, hX, hY, hZ, m);
		if(!blockWasPlaced){
			return false;
		}
		builder.buildStargate(w, x, y, z);
		w.setBlockMetadataWithNotify(x, y, z, ROTATIONS[dir.ordinal() - 2], 2);
		TileEntity te = w.getBlockTileEntity(x, y, z);
		((TileStargate)te).setDirectionX(isX);
		return true;
	}
}
