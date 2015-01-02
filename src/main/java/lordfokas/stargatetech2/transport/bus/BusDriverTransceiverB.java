package lordfokas.stargatetech2.transport.bus;

import java.util.LinkedList;

import lordfokas.stargatetech2.api.bus.BusPacket;
import lordfokas.stargatetech2.api.bus.IBusDriver;
import lordfokas.stargatetech2.transport.TileBeaconTransceiver;
import lordfokas.stargatetech2.transport.beacons.BeaconData;
import lordfokas.stargatetech2.transport.bus.BusPacketBeacons.RequestMode;

public class BusDriverTransceiverB implements IBusDriver{
	private TileBeaconTransceiver transceiver;
	private short address = (short) 0xFFFF;
	
	public BusDriverTransceiverB(TileBeaconTransceiver transceiver){
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
			if(beacons.mode == RequestMode.GET_ALL_BEACONS){
				LinkedList<BeaconData> list = transceiver.findAllBeacons();
				for(BeaconData beacon : list) beacons.addResponse(beacon);
			}else if(beacons.mode == RequestMode.GET_BEACON){
				BeaconData beacon = transceiver.findBeacon(beacons.beaconID);
				if(beacon != null) beacons.addResponse(beacon);
			}
		}
	}
	
	@Override public BusPacket getNextPacketToSend(){ return null; }
	
	@Override public boolean isInterfaceEnabled(){ return true; }
	
	@Override public short getInterfaceAddress(){ return address; }
	
	@Override
	public String getShortName(){
		return "Beacon Transceiver - Common Interface";
	}
	
	@Override
	public String getDescription(){
		return "The entry point for teleportation messages from the Transport Beacon Console.";
	}
}