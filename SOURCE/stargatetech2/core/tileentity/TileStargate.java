package stargatetech2.core.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import stargatetech2.common.base.BaseTileEntity;

public class TileStargate extends BaseTileEntity {
	public static final float dTheta = 1F;
	public float curr_theta = 0F;
	
	
	@Override
	public void updateEntity(){
		// ring rotation //
		/*curr_theta += dTheta;
		if(curr_theta > 360F){
			curr_theta -= 360F;
		}*/
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public double getMaxRenderDistanceSquared(){
		return 0x10000;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox(){
		return AxisAlignedBB.getAABBPool().getAABB(xCoord - 2, yCoord, zCoord-2, xCoord + 3, yCoord+5, zCoord+3);
	}
	
	@Override
	protected void readNBT(NBTTagCompound nbt) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void writeNBT(NBTTagCompound nbt) {
		// TODO Auto-generated method stub
	}
}