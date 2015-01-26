package lordfokas.stargatetech2.modules.automation;

public enum Connection {
	DISCONNECTED, DEVICE, CABLE;
	
	public boolean isConnected(){
		return this != DISCONNECTED;
	}
	
	public boolean hasPlug(){
		return this == DEVICE;
	}
}