package lordfokas.stargatetech2.ZZ_THRASH;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

@Deprecated
public class Vec4Int_THRASH extends Vec3Int_THRASH{
	public final int w;
	
	public Vec4Int_THRASH(int w, int x, int y, int z) {
		super(x, y, z);
		this.w = w;
	}
	
	public static Vec4Int_THRASH fromNBT(NBTTagCompound nbt){
		if(nbt.hasKey("w") && nbt.hasKey("x") && nbt.hasKey("y") && nbt.hasKey("z") && nbt.hasKey("Vec4Int")){
			int w = nbt.getInteger("w");
			int x = nbt.getInteger("x");
			int y = nbt.getInteger("y");
			int z = nbt.getInteger("z");
			return new Vec4Int_THRASH(w, x, y, z);
		}
		return null;
	}
	
	public NBTTagCompound toNBT(){
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("Vec4Int", "*");
		nbt.setInteger("w", w);
		nbt.setInteger("x", x);
		nbt.setInteger("y", y);
		nbt.setInteger("z", z);
		return nbt;
	}
	
	@Deprecated
	@Override
	public Vec4Int_THRASH offset(EnumFacing fd){
		return null;
	}
}
