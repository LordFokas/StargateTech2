package stargatetech2.core.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import stargatetech2.api.shields.ShieldPermissions;
import stargatetech2.common.base.BaseTileEntity;
import stargatetech2.common.util.Vec3Int;

public class TileShield extends BaseTileEntity {
	private Vec3Int emitter;
	
	@Override
	public boolean canUpdate(){
		return false;
	}
	
	@Override
	protected void readNBT(NBTTagCompound nbt) {
		if(nbt.hasKey("master"))
			emitter = Vec3Int.fromNBT(nbt.getCompoundTag("master"));
	}

	@Override
	protected void writeNBT(NBTTagCompound nbt) {
		if(emitter != null)
			nbt.setCompoundTag("master", emitter.toNBT());
	}
	
	public void setEmitter(Vec3Int emt){
		emitter = emt;
	}
	
	public TileShieldEmitter getEmitter(){
		if(emitter != null){
			TileEntity te = worldObj.getBlockTileEntity(emitter.x, emitter.y, emitter.z);
			if(te instanceof TileShieldEmitter)
				return (TileShieldEmitter) te;
			}
		return null;
	}
	
	public ShieldPermissions getPermissions(){
		if(emitter != null){
			TileShieldEmitter tse = getEmitter();
			if(tse != null){
				return tse.getPermissions();
			}
		}
		return ShieldPermissions.getDefault();
	}
}