package lordfokas.stargatetech2.lib.tileentity;

import net.minecraft.nbt.NBTTagCompound;

public interface ITileContext<T extends ITile>{
	public void tick();
	public void setTile(T tile);
	public boolean canTick();
	public void readNBTData(NBTTagCompound nbt);
	
	public static interface Client extends ITileContext<ITile.Client>{}
	
	public static interface Server extends ITileContext<ITile.Server>{
		public void writeNBTData(NBTTagCompound nbt);
	}
}