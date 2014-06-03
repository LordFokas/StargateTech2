package stargatetech2.enemy.bus;

import stargatetech2.api.bus.BusPacket;
import stargatetech2.api.bus.BusPacketLIP;
import stargatetech2.api.bus.BusProtocols;
import stargatetech2.api.bus.IBusDriver;
import stargatetech2.enemy.tileentity.TileShieldController;

public class ShieldControllerBusDriver implements IBusDriver{
	private TileShieldController controller;
	private short address = 0x0000;
	private boolean enabled = true;
	
	public ShieldControllerBusDriver(TileShieldController controller){
		this.controller = controller;
	}
	
	@Override
	public boolean canHandlePacket(short sender, int protocolID, boolean hasLIP) {
		return protocolID == BusPacketLIP.PROTOCOL_ID;
	}

	@Override
	public void handlePacket(BusPacket packet){
		BusPacketLIP lip = packet.getPlainText();
		String action = lip.get("action");
		if(action == null) return;
		if(action.equalsIgnoreCase("enable")){
			controller.setShieldStatus(true);
			lip.addResponse("Shield Status: Enabled");
		}else if(action.equalsIgnoreCase("disable")){
			controller.setShieldStatus(false);
			lip.addResponse("Shield Status: Disabled");
		}
	}

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
