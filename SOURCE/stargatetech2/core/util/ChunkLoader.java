package stargatetech2.core.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;
import stargatetech2.StargateTech2;
import stargatetech2.common.util.Helper;
import stargatetech2.core.network.stargate.StargateNetwork;

public final class ChunkLoader implements LoadingCallback{
	public static final ChunkLoader instance = new ChunkLoader();
	
	private ArrayList<Integer> dims = new ArrayList();
	private ArrayList<Ticket> tickets = new ArrayList();
	private long ticketID = 0;
	
	private ChunkLoader(){}
	
	public static void register(){
		ForgeChunkManager.setForcedChunkLoadingCallback(StargateTech2.instance, instance);
	}
	
	public static void load(){
		MinecraftForge.EVENT_BUS.register(instance);
		File file = Helper.getFile("chunks.dat");
		if(file.exists())
			try{
				FileInputStream fis = new FileInputStream(file);
				DataInputStream dis = new DataInputStream(fis);
				int dims = dis.readInt();
				for(int i = 0; i < dims; i++){
					instance.dims.add(dis.readInt());
				}
				dis.close();
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	
	public static void unload(){
		ArrayList<Integer> dims = new ArrayList();
		for(Ticket ticket : instance.tickets){
			Integer dim = ticket.world.provider.dimensionId;
			if(!dims.contains(dim)){
				dims.add(dim);
			}
		}
		try{
			File file = Helper.getFile("chunks.dat");
			if(!file.exists()){
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file);
			DataOutputStream dos = new DataOutputStream(fos);
			dos.writeInt(dims.size());
			for(Integer dim : dims){
				dos.writeInt(dim.intValue());
			}
			dos.close();
		}catch(Exception e){
			e.printStackTrace();
			
		}
	}
	
	@ForgeSubscribe
	public void forceReloadChunks(WorldEvent.Load evt){
		if(evt.world.provider.dimensionId == 0){
			MinecraftServer server = MinecraftServer.getServer();
			for(Integer dim : instance.dims){
				server.worldServerForDimension(dim.intValue());
			}
			StargateNetwork.instance().initializeWormholes();
			MinecraftForge.EVENT_BUS.unregister(this);
		}
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