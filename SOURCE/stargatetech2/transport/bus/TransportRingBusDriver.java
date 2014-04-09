package stargatetech2.transport.bus;

import stargatetech2.api.bus.BusPacket;
import stargatetech2.api.bus.BusPacketLIP;
import stargatetech2.api.bus.IBusDriver;
import stargatetech2.transport.tileentity.TileTransportRing;

public class TransportRingBusDriver implements IBusDriver{
	private TileTransportRing rings;
	
	public TransportRingBusDriver(TileTransportRing rings){
		this.rings = rings;
	}
	
	@Override
	public boolean canHandlePacket(short sender, int protocolID, boolean hasLIP) {
		return hasLIP;
	}
	
	@Override
	public void handlePacket(BusPacket packet) {
		BusPacketLIP lip = packet.getPlainText();
		String action = lip.get("action");
		if(action == null) return;
		boolean up;
		if(action.equalsIgnoreCase("link-down")) up = false;
		else if(action.equalsIgnoreCase("link-up")) up = true;
		else return;
		int leap;
		try{
			String lp = lip.get("skip");
			leap = Integer.parseInt(lp);
			leap++;
		}catch(Exception e){
			leap = 1;
		}
		rings.teleport(up, leap);
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
		return 0x0000;
	}
}