package stargatetech2.enemy.bus;

import stargatetech2.api.bus.BusPacket;
import stargatetech2.api.bus.IBusDriver;
import stargatetech2.core.util.Vec3Int;

public class ShieldEmitterBusDriver implements IBusDriver{
	@Override
	public boolean canHandlePacket(short sender, int protocolID, boolean hasLIP) {
		return sender == (short)0x0000 && protocolID == ShieldControllerBusPacket.PROTOCOL_ID;
	}

	@Override
	public void handlePacket(BusPacket packet) {
		boolean enabled = ((ShieldControllerBusPacket)packet).shieldsEnabled;
		Vec3Int controller = ((ShieldControllerBusPacket)packet).controller;
		// TODO: use the given state and controller coords to raise or lower the shields.
	}

	@Override public BusPacket getNextPacketToSend(){ return null; }
	@Override public boolean isInterfaceEnabled(){ return true; }
	@Override public short getInterfaceAddress(){ return (short)0x0000; }
}