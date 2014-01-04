package stargatetech2.core.util;

import java.util.ArrayList;
import java.util.List;

import stargatetech2.StargateTech2;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;

public final class ChunkLoader implements LoadingCallback{
	public static final ChunkLoader instance = new ChunkLoader();
	
	private ArrayList<Ticket> tickets = new ArrayList();
	private long ticketID = 0;
	
	private ChunkLoader(){}
	
	public static void register(){
		ForgeChunkManager.setForcedChunkLoadingCallback(StargateTech2.instance, instance);
	}
	
	public static void load(){
		
	}
	
	public static void unload(){
		
	}
	
	@Override
	public void ticketsLoaded(List<Ticket> tickets, World world) {
		for(Ticket ticket : tickets){
			instance.tickets.add(ticket);
			NBTTagCompound data = ticket.getModData();
			int chunks = data.getInteger("chunks");
			for(int c = 0; c < chunks; c++){
				int x = data.getInteger("cX_" + c);
				int z = data.getInteger("cZ_" + c);
				ForgeChunkManager.forceChunk(ticket, new ChunkCoordIntPair(x, z));
			}
		}
	}
	
	public static long load9Chunks(World world, int x, int z){
		ArrayList<ChunkCoordIntPair> chunks = new ArrayList();
		for(int i = -1; i < 2; i++){
			for(int j = -1; j < 2; j++){
				chunks.add(new ChunkCoordIntPair(x + i, z + j));
			}
		}
		return loadChunks(world, chunks);
	}
	
	public synchronized static long loadChunks(World world, List<ChunkCoordIntPair> chunks){
		Ticket ticket = ForgeChunkManager.requestTicket(StargateTech2.instance, world, Type.NORMAL);
		if(ticket != null){
			NBTTagCompound data = ticket.getModData();
			int chunkID = 0;
			data.setInteger("chunks", chunks.size());
			for(ChunkCoordIntPair chunk : chunks){
				ForgeChunkManager.forceChunk(ticket, chunk);
				data.setInteger("cX_" + chunkID, chunk.chunkXPos);
				data.setInteger("cZ_" + chunkID, chunk.chunkZPos);
			}
			long id = instance.ticketID;
			data.setLong("ticketID", id);
			instance.ticketID++;
			instance.tickets.add(ticket);
			return id;
		}
		return -1L;
	}
	
	public synchronized static void release(long ticket){
		Ticket found = null;
		for(Ticket t : instance.tickets){
			if(ticket == t.getModData().getLong("ticketID")){
				found = t;
				break;
			}
		}
		if(found == null) return;
		instance.tickets.remove(found);
		ForgeChunkManager.releaseTicket(found);
	}
}