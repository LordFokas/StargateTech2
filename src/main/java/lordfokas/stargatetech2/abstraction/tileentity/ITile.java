package lordfokas.stargatetech2.abstraction.tileentity;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public interface ITile{
	public Block getBlockType();
	public int getBlockMetadata();
	public World getWorld();
	public int x();
	public int y();
	public int z();
	
	public static interface Client extends ITile{}
	
	public static interface Server extends ITile{
		public void updateClients();
	}
}
