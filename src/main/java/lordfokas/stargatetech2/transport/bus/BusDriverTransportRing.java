package lordfokas.stargatetech2.transport.bus;

import lordfokas.stargatetech2.api.bus.BusPacket;
import lordfokas.stargatetech2.api.bus.BusPacketLIP;
import lordfokas.stargatetech2.api.bus.BusProtocols;
import lordfokas.stargatetech2.api.bus.IBusDriver;
import lordfokas.stargatetech2.transport.TileTransportRing;

public class BusDriverTransportRing implements IBusDriver{
	private TileTransportRing rings;
	
	public BusDriverTransportRing(TileTransportRing rings){
		this.rings = rings;
	}
	
	@Override
	public boolean canHandlePacket(short sender, int protocolID, boolean hasLIP) {
		return protocolID == BusProtocols.PROTOCOL_LIP;
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
		packet.addResponse("Ring activation request received");
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

	@Override
	public String getShortName() {
		return "Transport Rings";
	}

	@Override
	public String getDescription() {
		return "The Transport Rings can teleport any entity to another set of rings above or below itself.";
	}
}