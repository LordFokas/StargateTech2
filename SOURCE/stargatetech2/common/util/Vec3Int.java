package stargatetech2.common.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;

public class Vec3Int {
	public final int x, y, z;
	
	public Vec3Int(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public static Vec3Int fromNBT(NBTTagCompound nbt){
		if(nbt.hasKey("x") && nbt.hasKey("y") && nbt.hasKey("z") && nbt.hasKey("Vec3Int")){
			int x = nbt.getInteger("x");
			int y = nbt.getInteger("y");
			int z = nbt.getInteger("z");
			return new Vec3Int(x, y, z);
		}
		return null;
	}
	
	public NBTTagCompound toNBT(){
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("Vec3Int", "*");
		nbt.setInteger("x", x);
		nbt.setInteger("y", y);
		nbt.setInteger("z", z);
		return nbt;
	}
	
	public Vec3Int offset(ForgeDirection fd){
		return new Vec3Int(x + fd.offsetX, y + fd.offsetY, z + fd.offsetZ);
	}
}