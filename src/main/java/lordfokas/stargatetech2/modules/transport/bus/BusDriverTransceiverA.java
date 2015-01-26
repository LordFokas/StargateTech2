package lordfokas.stargatetech2.modules.transport.bus;

import lordfokas.stargatetech2.api.bus.BusPacket;
import lordfokas.stargatetech2.api.bus.IBusDriver;
import lordfokas.stargatetech2.modules.transport.TileBeaconTransceiver;
import lordfokas.stargatetech2.modules.transport.bus.BusPacketBeacons.RequestMode;

public class BusDriverTransceiverA implements IBusDriver{
	private TileBeaconTransceiver transceiver;
	private BusPacketBeacons packet = null;
	
	public BusDriverTransceiverA(TileBeaconTransceiver transceiver){
		this.transceiver = transceiver;
	}
	
	@Override
	public boolean canHandlePacket(short sender, int protocolID, boolean hasLIP){
		return protocolID == BusPacketBeacons.PROTOCOL_ID;
	}
	
	@Override
	public void handlePacket(BusPacket packet){
		if(packet instanceof BusPacketBeacons){
			BusPacketBeacons beacons = (BusPacketBeacons) packet;
			if(beacons.mode == RequestMode.SCAN_BEACONS){
				beacons.addResponse(transceiver.getData());
			}
		}
	}
	
	public void sendPacket(BusPacketBeacons packet){
		this.packet = packet;
	}
	
	@Override
	public BusPacket getNextPacketToSend(){
		BusPacketBeacons packet = this.packet;
		this.packet = null;
		return packet;
	}
	
	@Override public boolean isInterfaceEnabled(){ return true; }
	
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