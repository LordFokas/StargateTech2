package lordfokas.stargatetech2.transport.bus;

import lordfokas.stargatetech2.api.bus.BusPacket;
import lordfokas.stargatetech2.api.bus.IBusDriver;

public class BusDriverTransceiverA implements IBusDriver{

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
		return 0x0000; // We're sniffing on this interface.
	}

	@Override
	public String getShortName() {
		return "Beacon Transceiver - Antenna Interface";
	}

	@Override
	public String getDescription() {
		return "Is used to / can only communicate with other beacon Transceivers.";
	}
}
