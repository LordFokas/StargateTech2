package lordfokas.stargatetech2.enemy;

import lordfokas.stargatetech2.api.shields.ShieldPermissions;
import lordfokas.stargatetech2.core.Vec3Int;
import lordfokas.stargatetech2.core.base.BaseTileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileShield extends BaseTileEntity {
	private Vec3Int controller;
	
	@Override
	public boolean canUpdate(){
		return false;
	}
	
	@Override
	protected void readNBT(NBTTagCompound nbt) {
		if(nbt.hasKey("master"))
			controller = Vec3Int.fromNBT(nbt.getCompoundTag("master"));
	}

	@Override
	protected void writeNBT(NBTTagCompound nbt) {
		if(controller != null)
			nbt.setTag("master", controller.toNBT());
	}
	
	public void setController(Vec3Int controller){
		this.controller = controller;
	}
	
	public TileShieldController getController(){
		if(controller != null){
			TileEntity te = worldObj.getTileEntity(controller.x, controller.y, controller.z);
			if(te instanceof TileShieldController)
				return (TileShieldController) te;
			}
		return null;
	}
	
	public ShieldPermissions getPermissions(){
		if(controller != null){
			TileShieldController tsc = getController();
			if(tsc != null){
				return tsc.getPermissions();
			}
		}
		return ShieldPermissions.getDefault();
	}
	
	public String getOwner(){
		TileShieldController controller = getController();
		if(controller == null) return "";
		else return controller.getOwner();
	}
}