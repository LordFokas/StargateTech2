package stargatetech2.core.network.stargate;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import stargatetech2.api.stargate.Address;
import stargatetech2.common.util.Vec3Int;
import stargatetech2.core.tileentity.TileStargate;
import stargatetech2.core.util.ChunkLoader;
import stargatetech2.core.util.Teleporter;

public final class Wormhole {
	private static final int DEFAULT_WORMHOLE_TIME = 760; // 38 seconds
	
	private final TileStargate source, destination;
	private boolean isWormholeActive = true;
	private long lastWormholeTime = 0;
	private long countdown, src, dst;
	
	public Wormhole(TileStargate source, TileStargate destination, long srcChunks, long dstChunks){
		this.countdown = DEFAULT_WORMHOLE_TIME;
		this.source = source;
		this.destination = destination;
		this.src = srcChunks;
		this.dst = dstChunks;
		lastWormholeTime = source.worldObj.getTotalWorldTime();
		System.out.println("Adding new wormhole");
		source.setWormhole(this, true);
		destination.setWormhole(this, false);
	}
	
	public void update(){
		countdown -= (source.worldObj.getTotalWorldTime() - lastWormholeTime);
		lastWormholeTime = source.worldObj.getTotalWorldTime();
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
			aabb = AxisAlignedBB.getBoundingBox(source.xCoord-1, source.yCoord+1, source.zCoord, source.xCoord+2, source.yCoord+4, source.zCoord+1);
		}else{
			aabb = AxisAlignedBB.getBoundingBox(source.xCoord, source.yCoord+1, source.zCoord-1, source.xCoord+1, source.yCoord+4, source.zCoord+2);
		}
		Vec3Int position = new Vec3Int(destination.xCoord, destination.yCoord+1, destination.zCoord);
		float yaw = (90 * destination.getBlockMetadata()) - 180;
		List<Entity> entities = source.worldObj.getEntitiesWithinAABB(Entity.class, aabb);
		for(Entity entity : entities){
			if(entity.riddenByEntity == null){
				Teleporter.teleport(source.worldObj, entity, destination.worldObj, position, yaw);
			}
		}
	}
}