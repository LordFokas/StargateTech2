package lordfokas.stargatetech2.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

@Deprecated
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
	
	public Vec3Int offset(EnumFacing fd){
		return new Vec3Int(x + fd.getFrontOffsetX(), y + fd.getFrontOffsetY(), z + fd.getFrontOffsetZ());
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Vec3Int){
			Vec3Int v = (Vec3Int) o;
			return x == v.x && y == v.y && z == v.z;
		}
		return false;
	}
	
	@Override
	public String toString(){
		return "Vec3Int{ " + x + " : " + y + " : " + z + " }";
	}
}