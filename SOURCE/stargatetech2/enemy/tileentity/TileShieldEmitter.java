package stargatetech2.enemy.tileentity;

import java.util.LinkedList;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import stargatetech2.core.machine.FaceColor;
import stargatetech2.core.machine.TileMachine;
import stargatetech2.core.util.Vec3Int;
import stargatetech2.enemy.util.IShieldControllerProvider;

public class TileShieldEmitter extends TileMachine implements IShieldControllerProvider{
	private Vec3Int controller;
	
	public void setController(Vec3Int controller){
		this.controller = controller;
		TileEntity te = worldObj.getBlockTileEntity(controller.x, controller.y, controller.z);
		if(te instanceof TileShieldController){
			((TileShieldController)te).addEmitter(this);
		}
	}
	
	@Override
	public void invalidate(){
		super.invalidate();
		if(controller == null) return;
		TileEntity te = worldObj.getBlockTileEntity(controller.x, controller.y, controller.z);
		if(te instanceof TileShieldController){
			((TileShieldController)te).removeEmitter(this);
		}
	}
	
	public LinkedList<Vec3Int> createShields(){
		return null; // TODO: implement this
	}
	
	@Override public boolean canUpdate(){ return false; }
	
	@Override // Never used;
	protected FaceColor[] getPossibleFaceColors(){ return null; }

	@Override
	protected void readNBT(NBTTagCompound nbt){
		controller = Vec3Int.fromNBT(nbt.getCompoundTag("controller"));
	}

	@Override
	protected void writeNBT(NBTTagCompound nbt){
		nbt.setCompoundTag("controller", controller.toNBT());
	}
	
	@Override
	public Vec3Int getShieldControllerCoords(){
		return controller;
	}
}