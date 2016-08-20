package lordfokas.naquadria.tileentity;

import net.minecraft.block.Block;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

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
	public BlockPos pos();
	
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
