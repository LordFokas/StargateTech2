package lordfokas.stargatetech2.modules.automation;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

public class RemoteDevice implements INBTSerializable<NBTTagCompound>{
	public BlockPos pos;
	public EnumFacing side;
	
	public RemoteDevice(BlockPos pos, EnumFacing side) {
		this.pos = pos;
		this.side = side;
	}
	
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("x", pos.getX());
		nbt.setInteger("y", pos.getY());
		nbt.setInteger("z", pos.getZ());
		nbt.setInteger("s", side.ordinal());
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		int x, y, z;
		x = nbt.getInteger("x");
		y = nbt.getInteger("y");
		z = nbt.getInteger("z");
		pos = new BlockPos(x, y, z);
		side = EnumFacing.values()[nbt.getInteger("s")];
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pos == null) ? 0 : pos.hashCode());
		result = prime * result + ((side == null) ? 0 : side.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RemoteDevice other = (RemoteDevice) obj;
		if (pos == null) {
			if (other.pos != null)
				return false;
		} else if (!pos.equals(other.pos))
			return false;
		if (side != other.side)
			return false;
		return true;
	}
}
