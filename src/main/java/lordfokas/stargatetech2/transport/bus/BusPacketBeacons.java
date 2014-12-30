package lordfokas.stargatetech2.transport.bus;

import lordfokas.stargatetech2.api.bus.BusPacket;
import lordfokas.stargatetech2.api.bus.BusPacketLIP;
import lordfokas.stargatetech2.api.bus.BusProtocols;
import lordfokas.stargatetech2.transport.beacons.BeaconData;

public class BusPacketBeacons extends BusPacket<BeaconData>{
	public static final int PROTOCOL_ID = BusProtocols.addProtocol(BusPacketBeacons.class);
	
	public final RequestMode mode;
	public final String beaconID;
	
	public enum RequestMode{
		GET_ALL_BEACONS, GET_BEACON, SCAN_BEACONS
	}
	
	public BusPacketBeacons(short sender, short target){
		this(sender, target, RequestMode.GET_ALL_BEACONS, null);
	}
	
	public BusPacketBeacons(short sender, short target, String beaconID){
		this(sender, target, RequestMode.GET_BEACON, beaconID);
	}
	
	public BusPacketBeacons(short sender, short target, RequestMode mode, String beaconID) {
		super(sender, target, false);
		this.mode = mode;
		this.beaconID = beaconID;
	}
	
	@Override protected void fillPlainText(BusPacketLIP lip){}
}
