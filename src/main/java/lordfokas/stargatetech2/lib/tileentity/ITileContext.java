package lordfokas.stargatetech2.lib.tileentity;

import net.minecraft.nbt.NBTTagCompound;

public interface ITileContext{
	public void tick();
	public void setTile(ITile tile);
	public boolean canTick();
	public void readNBTData(NBTTagCompound nbt);
	public void writeNBTData(NBTTagCompound nbt);
	
	public static interface Client extends ITileContext{
		public void setTile(ITile.Client tile);
	}
	
	public static interface Server extends ITileContext{
		public void setTile(ITile.Server tile);
	}
}