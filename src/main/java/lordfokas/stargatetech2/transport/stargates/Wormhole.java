package lordfokas.stargatetech2.transport.stargates;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import lordfokas.stargatetech2.api.stargate.Address;
import lordfokas.stargatetech2.core.util.ChunkLoader;
import lordfokas.stargatetech2.core.util.StargateLogger;
import lordfokas.stargatetech2.core.util.Vec3Int;
import lordfokas.stargatetech2.core.util.Vec4Int;
import lordfokas.stargatetech2.transport.tileentity.TileStargate;
import lordfokas.stargatetech2.transport.util.Teleporter;

public final class Wormhole {
	private Vec4Int tmpSrc, tmpDst;
	private TileStargate source, destination;
	private boolean isWormholeActive = true;
	private long lastWormholeTime = 0;
	private long countdown, src, dst;
	
	private Wormhole(){}
	
	public Wormhole(TileStargate source, TileStargate destination, long srcChunks, long dstChunks, int timeout){
		this.countdown = timeout * 20;
		this.source = source;
		this.destination = destination;
		this.src = srcChunks;
		this.dst = dstChunks;
		lastWormholeTime = source.getWorldObj().getTotalWorldTime();
		source.setWormhole(this, true, true);
		destination.setWormhole(this, false, true);
	}
	
	public void update(){
		countdown -= (source.getWorldObj().getTotalWorldTime() - lastWormholeTime);
		lastWormholeTime = source.getWorldObj().getTotalWorldTime();
		doTeleport();
		if(countdown <= 0){
			disconnect();
		}
	}
	
	public void disconnect(){
		isWormholeActive = false;
		source.onDisconnect();
		destination.onDisconnect();
		StargateNetwork.instance().removeWormhole(this);
		ChunkLoader.release(src);
		ChunkLoader.release(dst);
	}
	
	public boolean isActive(){
		return isWormholeActive;
	}
	
	public Address getSourceAddress(){
		return source.getAddress();
	}
	
	public Address getDestinationAddress(){
		return destination.getAddress();
	}
	
	private void doTeleport(){
		AxisAlignedBB aabb;
		if(source.isX()){
			aabb = AxisAlignedBB.getBoundingBox(source.xCoord-1, source.yCoord+1, source.zCoord+0.375, source.xCoord+2, source.yCoord+4, source.zCoord+0.625);
		}else{
			aabb = AxisAlignedBB.getBoundingBox(source.xCoord+0.375, source.yCoord+1, source.zCoord-1, source.xCoord+0.625, source.yCoord+4, source.zCoord+2);
		}
		Vec3Int position = new Vec3Int(destination.xCoord, destination.yCoord+1, destination.zCoord);
		float yaw = (90 * destination.getBlockMetadata());
		List<Entity> entities = source.getWorldObj().getEntitiesWithinAABB(Entity.class, aabb);
		for(Entity entity : entities){
			if(entity.riddenByEntity == null){
				Teleporter.teleport(source.getWorldObj(), entity, destination.getWorldObj(), position, yaw);
			}
		}
	}
	
	public void writeToStream(DataOutputStream dos) throws Exception{
		dos.writeLong(countdown);
		dos.writeLong(lastWormholeTime);
		dos.writeLong(dst);
		dos.writeLong(src);
		dos.writeInt(source.getWorldObj().provider.dimensionId);
		dos.writeInt(source.xCoord);
		dos.writeInt(source.yCoord);
		dos.writeInt(source.zCoord);
		dos.writeInt(destination.getWorldObj().provider.dimensionId);
		dos.writeInt(destination.xCoord);
		dos.writeInt(destination.yCoord);
		dos.writeInt(destination.zCoord);
	}
	
	public static Wormhole readFromStream(DataInputStream dis) throws Exception{
		int w, x, y, z;
		Wormhole wh = new Wormhole();
		wh.countdown = dis.readLong();
		wh.lastWormholeTime = dis.readLong();
		wh.dst = dis.readLong();
		wh.src = dis.readLong();
		w = dis.readInt();
		x = dis.readInt();
		y = dis.readInt();
		z = dis.readInt();
		wh.tmpSrc = new Vec4Int(w, x, y, z);
		w = dis.readInt();
		x = dis.readInt();
		y = dis.readInt();
		z = dis.readInt();
		wh.tmpDst = new Vec4Int(w, x, y, z);
		return wh;
	}
	
	public void initialize(){
		try{
			World w;
			TileStargate s;
			w = MinecraftServer.getServer().worldServerForDimension(tmpSrc.w);
			s = (TileStargate) w.getTileEntity(tmpSrc.x, tmpSrc.y, tmpSrc.z);
			source = s;
			w = MinecraftServer.getServer().worldServerForDimension(tmpDst.w);
			s = (TileStargate) w.getTileEntity(tmpDst.x, tmpDst.y, tmpDst.z);
			destination = s;
			source.setWormhole(this, true, false);
			destination.setWormhole(this, false, false);
		}catch(Exception e){
			StargateNetwork.instance().removeWormhole(this);
			StargateLogger.error("Error while initializing a wormhole!");
			e.printStackTrace();
		}
	}
}