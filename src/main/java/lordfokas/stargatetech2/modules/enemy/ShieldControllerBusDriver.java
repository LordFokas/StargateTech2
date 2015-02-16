package lordfokas.stargatetech2.modules.enemy;

import lordfokas.stargatetech2.api.bus.BusPacket;
import lordfokas.stargatetech2.api.bus.BusPacketLIP;
import lordfokas.stargatetech2.api.bus.BusProtocols;
import lordfokas.stargatetech2.modules.automation.ISyncBusDriver;
import lordfokas.stargatetech2.modules.enemy.tileentity.ShieldControllerCommon;

public class ShieldControllerBusDriver implements ISyncBusDriver{
	private ShieldControllerCommon controller;
	private short address = 0x0000;
	private boolean enabled = true;
	
	public ShieldControllerBusDriver(ShieldControllerCommon controller){
		this.controller = controller;
	}
	
	@Override
	public boolean canHandlePacket(short sender, int protocolID, boolean hasLIP) {
		return protocolID == BusProtocols.PROTOCOL_LIP;
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

	@Override
	public String getShortName() {
		return "Shield Controller";
	}

	@Override
	public String getDescription() {
		return "The Shield Controller is used to control attached Shield Emitters.";
	}

	@Override
	public void setInterfaceAddress(short address) {
		this.address = address;
	}

	@Override
	public void setInterfaceEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
