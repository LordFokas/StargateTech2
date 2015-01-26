package lordfokas.stargatetech2.modules.transport;

import lordfokas.stargatetech2.modules.transport.stargates.StargateNetwork;
import lordfokas.stargatetech2.modules.world.genlists.StargateBuildList;
import lordfokas.stargatetech2.util.Helper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemBlockStargate extends ItemBlock {
	public static ItemBlockStargate instance;
	private static final int[] ROTATIONS = new int[]{4, 2, 5, 3};
	
	public ItemBlockStargate(Block b) {
		super(b);
		instance = this;
	}
	
	@Override
	public boolean placeBlockAt(ItemStack i, EntityPlayer p, World w, int x, int y, int z, int s, float hX, float hY, float hZ, int m){
		if(!StargateNetwork.instance().canPlaceStargateAt(w, x, y, z)){
			return false;
		}
		boolean blockWasPlaced;
		ForgeDirection dir = Helper.yaw2dir(p.rotationYaw, 0, false);
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
		TileEntity te = w.getTileEntity(x, y, z);
		((TileStargate)te).setDirectionX(isX);
		return true;
	}
}
