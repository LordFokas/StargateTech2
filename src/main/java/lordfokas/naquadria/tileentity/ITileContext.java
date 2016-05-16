package lordfokas.naquadria.tileentity;

import net.minecraft.nbt.NBTTagCompound;

/**
 * The TileEntity Context. It works like a controller.
 * The TileEntity supplies many small services and the
 * context gives the orders.
 * 
 * @author LordFokas
 */
public interface ITileContext{
	public void tick();
	public void setTile(ITile tile);
	public boolean canTick();
	public void readNBTData(NBTTagCompound nbt);
	public void writeNBTData(NBTTagCompound nbt);
	
	/**
	 * A context on a client world.
	 * 
	 * @author LordFokas
	 */
	public static interface Client extends ITileContext{
		public void setTile(ITile.Client tile);
	}
	
	/**
	 * A context on a server world.
	 * 
	 * @author LordFokas
	 */
	public static interface Server extends ITileContext{
		public void setTile(ITile.Server tile);
	}
}