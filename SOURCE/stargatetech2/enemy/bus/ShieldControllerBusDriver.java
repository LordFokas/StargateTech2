package stargatetech2.enemy.bus;

import stargatetech2.api.bus.BusPacket;
import stargatetech2.api.bus.IBusDriver;

public class ShieldControllerBusDriver implements IBusDriver{
	private short address = 0x0000;
	private boolean enabled = true;
	
	@Override
	public boolean canHandlePacket(short sender, int protocolID, boolean hasLIP) {
		return false;
	}

	@Override
	public void handlePacket(BusPacket packet){}

	@Override
	public BusPacket getNextPacketToSend() {
		return null;
	}

	@Override
	public boolean isInterfaceEnabled() {
		return enabled;
	}

	@Override
	public short getInterfaceAddress() {
		return address;
	}
	
	public void setEnabled(boolean enabled){
		this.enabled = enabled;
	}
	
	public void setAddress(short address){
		this.address = address;
	}
}
