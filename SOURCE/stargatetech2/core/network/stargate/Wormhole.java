package stargatetech2.core.network.stargate;

import stargatetech2.core.tileentity.TileStargate;

public final class Wormhole {
	private static final int DEFAULT_WORMHOLE_TIME = 38;
	
	private final TileStargate source, destination;
	private boolean isWormholeActive = true;
	private long lastWormholeTime = 0;
	private long countdown;
	
	public Wormhole(TileStargate source, TileStargate destination){
		this.countdown = DEFAULT_WORMHOLE_TIME;
		this.source = source;
		this.destination = destination;
		lastWormholeTime = source.worldObj.getTotalWorldTime();
		source.setWormhole(this, true);
		destination.setWormhole(this, false);
	}
	
	public void update(){
		countdown -= (source.worldObj.getTotalWorldTime() - lastWormholeTime);
		lastWormholeTime = source.worldObj.getTotalWorldTime();
		if(countdown <= 0){
			disconnect();
		}
	}
	
	public void disconnect(){
		isWormholeActive = false;
		source.onDisconnect();
		destination.onDisconnect();
		StargateNetwork.instance().removeWormhole(this);
	}
	
	public boolean isActive(){
		return isWormholeActive;
	}
}