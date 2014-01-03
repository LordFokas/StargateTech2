package stargatetech2.core.network.bus.machines;

import java.util.LinkedList;

import stargatetech2.api.bus.BusPacket;
import stargatetech2.api.bus.BusPacketLIP;
import stargatetech2.api.bus.IBusDriver;
import stargatetech2.api.stargate.Address;
import stargatetech2.core.network.stargate.StargateNetwork;
import stargatetech2.core.tileentity.TileStargate;

public class StargateBusDriver implements IBusDriver{
	private LinkedList<BusPacket> queue = new LinkedList();
	private TileStargate stargate;
	
	public StargateBusDriver(TileStargate stargate){
		this.stargate = stargate;
	}
	
	@Override
	public boolean canHandlePacket(short sender, int protocolID, boolean hasLIP) {
		return protocolID == BusPacketLIP.PROTOCOL_ID;
	}

	@Override
	public void handlePacket(BusPacket packet) {
		String dial = packet.getPlainText().get("dial");
		if(dial != null){
			Address address = StargateNetwork.parse(dial);
			if(address != null){
				System.out.println("Attempting to dial " + address.toString());
				stargate.dial(address);
			}
		}
	}

	@Override
	public BusPacket getNextPacketToSend() {
		return queue.isEmpty() ? null : queue.removeFirst();
	}
	
	public void addPacket(BusPacket packet){
		queue.addLast(packet);
	}
	
	@Override
	public boolean isInterfaceEnabled() {
		return true;
	}

	@Override
	public short getInterfaceAddress() {
		return 0x0000; // Sniffing only.
	}
}