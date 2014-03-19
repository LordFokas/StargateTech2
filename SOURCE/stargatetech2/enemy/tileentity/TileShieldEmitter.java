package stargatetech2.enemy.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import stargatetech2.core.machine.FaceColor;
import stargatetech2.core.machine.TileEntityMachine;
import stargatetech2.core.util.Vec3Int;

public class TileShieldEmitter extends TileEntityMachine{
	private Vec3Int controllerCoords;
	
	@Override
	public boolean canUpdate(){
		return false;
	}
	
	@Override // Never used;
	protected FaceColor[] getPossibleFaceColors(){
		return null;
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