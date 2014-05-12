package stargatetech2.enemy.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import stargatetech2.core.machine.FaceColor;
import stargatetech2.core.machine.TileMachine;
import stargatetech2.core.util.Vec3Int;
import stargatetech2.enemy.util.IShieldControllerProvider;

public class TileShieldEmitter extends TileMachine implements IShieldControllerProvider{
	private Vec3Int controller;
	
	public void setController(Vec3Int controller){
		this.controller = controller;
	}
	
	@Override
	public boolean canUpdate(){
		return false;
	}
	
	@Override // Never used;
	protected FaceColor[] getPossibleFaceColors(){ return null; }

	@Override
	protected void readNBT(NBTTagCompound nbt){
		// TODO Auto-generated method stub
	}

	@Override
	protected void writeNBT(NBTTagCompound nbt){
		// TODO Auto-generated method stub
	}

	@Override
	public Vec3Int getShieldControllerCoords(){
		return controller;
	}
}