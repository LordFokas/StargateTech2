package lordfokas.stargatetech2.transport;

import lordfokas.stargatetech2.api.bus.BusPacket;
import lordfokas.stargatetech2.api.bus.IBusDriver;

public class BusDriverTransceiverB implements IBusDriver{
	private short address = (short) 0xFFFF;
	
	@Override
	public boolean canHandlePacket(short sender, int protocolID, boolean hasLIP) {
		return false;
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
		return address;
	}

	@Override
	public String getShortName() {
		return "Beacon Transceiver - Common Interface";
	}

	@Override
	public String getDescription() {
		return "The entry point for teleportation messages from the Transport Beacon Console.";
	}
}