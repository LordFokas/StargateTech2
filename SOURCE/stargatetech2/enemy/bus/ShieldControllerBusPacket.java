package stargatetech2.enemy.bus;

import stargatetech2.api.bus.BusPacket;
import stargatetech2.api.bus.BusPacketLIP;
import stargatetech2.api.bus.BusProtocols;
import stargatetech2.core.util.Vec3Int;

public class ShieldControllerBusPacket extends BusPacket {
	public static final int PROTOCOL_ID = BusProtocols.addProtocol(ShieldControllerBusPacket.class);
	public final boolean shieldsEnabled;
	public final Vec3Int controller;
	
	public ShieldControllerBusPacket(boolean shieldsEnabled, Vec3Int controller){
		super((short)0x0000, (short)0x0000, false);
		this.shieldsEnabled = shieldsEnabled;
		this.controller = controller;
	}

	@Override protected void fillPlainText(BusPacketLIP lip){}
}