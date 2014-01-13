package stargatetech2.core.network.bus.machines;

import stargatetech2.api.bus.BusPacket;
import stargatetech2.api.bus.BusPacketLIP;
import stargatetech2.api.bus.IBusDriver;
import stargatetech2.core.tileentity.TileShieldEmitter;

public class ShieldEmitterBusDriver implements IBusDriver{
	private TileShieldEmitter shield;
	
	public ShieldEmitterBusDriver(TileShieldEmitter shield){
		this.shield = shield;
	}
	
	@Override
	public boolean canHandlePacket(short sender, int protocolID, boolean hasLIP) {
		return hasLIP;
	}

	@Override
	public void handlePacket(BusPacket packet) {
		BusPacketLIP lip = packet.getPlainText();
		String action = lip.get("action");
		if(action != null){
			action = action.toLowerCase();
			if(action.contentEquals("stop")){
				shield.setCanRun(false);
			}else if(action.contentEquals("start")){
				shield.setCanRun(true);
			}
		}
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
