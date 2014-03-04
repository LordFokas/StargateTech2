package stargatetech2.core.machine;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import stargatetech2.core.base.BaseTileEntity;
import stargatetech2.core.packet.PacketUpdateMachineColors;

public abstract class TileEntityMachine extends BaseTileEntity {
	private FaceColor[] colors = new FaceColor[]{
			FaceColor.VOID, FaceColor.VOID,
			FaceColor.VOID, FaceColor.VOID,
			FaceColor.VOID, FaceColor.VOID,
	};
	
	public final FaceColor getColor(ForgeDirection side){
		return getColor(side.ordinal());
	}
	
	public final FaceColor getColor(int side) {
		return colors[side];
	}
	
	protected void setColor(int side, FaceColor color){
		colors[side] = color != null ? color : FaceColor.VOID;
		forceRedraw();
	}
	
	protected final void forceRedraw(){
		if(!worldObj.isRemote){
			PacketUpdateMachineColors update = new PacketUpdateMachineColors(colors);
			update.x = xCoord;
			update.y = yCoord;
			update.z = zCoord;
			update.sendToAllInDim(worldObj.provider.dimensionId);
			worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType().blockID);
		}
	}
	
	public final void updateColors(FaceColor[] colors){
		this.colors = colors;
		worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
	}
	
	protected final NBTTagCompound writeFacingNBT(){
		NBTTagCompound nbt = new NBTTagCompound();
		for(int i = 0; i < colors.length; i++){
			nbt.setByte("color" + i, (byte) colors[i].ordinal());
		}
		return nbt;
	}
	
	protected final void readFacingNBT(NBTTagCompound nbt){
		for(int i = 0; i < colors.length; i++){
			colors[i] = FaceColor.values()[nbt.getByte("color" + i)];
		}
	}
}