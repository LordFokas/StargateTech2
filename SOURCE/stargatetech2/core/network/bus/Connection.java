package stargatetech2.core.network.bus;

public enum Connection {
	DISCONNECTED, DEVICE, CABLE;
	
	public boolean isConnected(){
		return this != DISCONNECTED;
	}
	
	public boolean hasPlug(){
		return this == DEVICE;
	}
}