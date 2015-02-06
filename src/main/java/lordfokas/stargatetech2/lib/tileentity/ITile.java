package lordfokas.stargatetech2.lib.tileentity;

import net.minecraft.block.Block;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.world.World;

/**
 * Represents a generic TileEntity.
 * Is used by {@link ITileContext} instances to gather data from the world.
 * 
 * @author LordFokas
 */
public interface ITile{
	public Block getBlockType();
	public int getBlockMetadata();
	public World getWorld();
	public int x();
	public int y();
	public int z();
	
	/**
	 * An {@link ITile} on a {@link WorldClient}
	 * 
	 * @author LordFokas
	 */
	public static interface Client extends ITile{}
	
	/**
	 * An {@link ITile} on a {@link WorldServer}
	 * Can trigger updates to the clients.
	 * 
	 * @author LordFokas
	 */
	public static interface Server extends ITile{
		public void updateClients();
	}
}
