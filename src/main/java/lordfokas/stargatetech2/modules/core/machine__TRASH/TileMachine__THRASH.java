package lordfokas.stargatetech2.modules.core.machine__TRASH;

import lordfokas.stargatetech2.modules.core.base__THRASH.BaseTileEntity__OLD_AND_FLAWED;
import lordfokas.stargatetech2.modules.core.packets__THRASH.PacketUpdateMachineColors__THRASH;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class TileMachine__THRASH extends BaseTileEntity__OLD_AND_FLAWED {
	private FaceColor__THRASH[] colors = new FaceColor__THRASH[]{
			FaceColor__THRASH.VOID, FaceColor__THRASH.VOID,
			FaceColor__THRASH.VOID, FaceColor__THRASH.VOID,
			FaceColor__THRASH.VOID, FaceColor__THRASH.VOID,
	};
	
	protected abstract FaceColor__THRASH[] getPossibleFaceColors();
	
	public final boolean hasColor(FaceColor__THRASH color){
		for(FaceColor__THRASH c : colors){
			if(c == color) return true;
		}
		return false;
	}
	
	@SuppressWarnings("incomplete-switch")
	public final int getSideFromFace(Face__THRASH face){
		ForgeDirection dir = null;
		if(face == Face__THRASH.TOP){
			dir = ForgeDirection.UP;
		}else if(face == Face__THRASH.BOTTOM){
			dir = ForgeDirection.DOWN;
		}else if(face == Face__THRASH.FRONT){
			dir = ForgeDirection.getOrientation(getBlockMetadata());
		}else if(face == Face__THRASH.BACK){
			dir = ForgeDirection.getOrientation(getBlockMetadata()).getOpposite();
		}else{
			dir = ForgeDirection.getOrientation(getBlockMetadata());
			ForgeDirection left = ForgeDirection.UNKNOWN;
			switch(dir){
				case EAST:
					left = ForgeDirection.SOUTH;
					break;
				case NORTH:
					left = ForgeDirection.EAST;
					break;
				case SOUTH:
					left = ForgeDirection.WEST;
					break;
				case WEST:
					left = ForgeDirection.NORTH;
					break;
			}
			dir = (face == Face__THRASH.LEFT) ? left : left.getOpposite();
		}
		return dir.ordinal();
	}
	
	public final void toggleFace(Face__THRASH face){
		FaceColor__THRASH[] possibleColors = getPossibleFaceColors();
		int side = getSideFromFace(face);
		FaceColor__THRASH currentColor = getColor(side);
		for(int index = 0; index < possibleColors.length; index++){
			if(currentColor == possibleColors[index]){
				int next = (index + 1) % possibleColors.length;
				setColor(side, possibleColors[next]);
				break;
			}
		}
	}
	
	public final FaceColor__THRASH getColor(Face__THRASH face){
		return getColor(getSideFromFace(face));
	}
	
	public final FaceColor__THRASH getColor(ForgeDirection side){
		return getColor(side.ordinal());
	}
	
	public final FaceColor__THRASH getColor(int side) {
		if(side < 0 || side >= colors.length){
			return FaceColor__THRASH.VOID;
		}
		return colors[side];
	}
	
	protected void setColor(int side, FaceColor__THRASH color){
		colors[side] = color != null ? color : FaceColor__THRASH.VOID;
		forceRedraw();
	}
	
	protected final void forceRedraw(){
		if(!worldObj.isRemote){
			PacketUpdateMachineColors__THRASH update = new PacketUpdateMachineColors__THRASH();
			update.x = xCoord;
			update.y = yCoord;
			update.z = zCoord;
			update.sendToClientsInDim(worldObj.provider.dimensionId);
			worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType());
		}
	}
	
	public final void updateColors(FaceColor__THRASH[] colors){
		this.colors = colors;
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
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
			colors[i] = FaceColor__THRASH.values()[nbt.getByte("color" + i)];
		}
	}
}