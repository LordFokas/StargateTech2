package stargatetech2.core.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import stargatetech2.common.base.BaseTileEntity;
import stargatetech2.common.util.Vec3Int;
import stargatetech2.core.util.ShieldPermissions;

public class TileShield extends BaseTileEntity {
	private Vec3Int emitter;
	
	@Override
	public boolean canUpdate(){
		return false;
	}
	
	@Override
	protected void readNBT(NBTTagCompound nbt) {
		emitter = Vec3Int.fromNBT(nbt.getCompoundTag("master"));
	}

	@Override
	protected void writeNBT(NBTTagCompound nbt) {
		nbt.setCompoundTag("master", emitter.toNBT());
	}
	
	public void setEmitter(Vec3Int emt){
		emitter = emt;
	}
	
	public ShieldPermissions getPermissions(){
		if(emitter != null){
			TileEntity te = worldObj.getBlockTileEntity(emitter.x, emitter.y, emitter.z);
			if(te instanceof TileShieldEmitter){
				return ((TileShieldEmitter)te).getPermissions();
			}
		}
		return ShieldPermissions.getDefault();
	}
}