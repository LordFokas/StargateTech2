package stargatetech2.automation.bus;

import stargatetech2.api.bus.BusPacket;
import stargatetech2.api.bus.IBusDriver;

public class BusDriver implements IBusDriver{
	public BusDriver(){}
	
	@Override
	public boolean canHandlePacket(short sender, int protocolID, boolean hasLIP) {
		return hasLIP;
	}

	@Override
	public void handlePacket(BusPacket packet) {
		
	}

	@Override
	public BusPacket getNextPacketToSend() {
		return null;
	}

	@Override
	public boolean isInterfaceEnabled() {
		return true;
	}

	@Override
	public short getInterfaceAddress() {
		return 0x7F01;
	}
}