package lordfokas.stargatetech2.transport.beacons;

import lordfokas.stargatetech2.transport.TileBeaconTransceiver;

public class BeaconData {
	public final TileBeaconTransceiver transceiver;
	public final String beaconID;
	public final int color;
	public final int icon;
	
	public BeaconData(TileBeaconTransceiver transceiver, String beaconID, int color, int icon){
		this.transceiver = transceiver;
		this.beaconID = beaconID;
		this.color = color;
		this.icon = icon;
	}
}
