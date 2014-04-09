package stargatetech2.automation.bus;

import java.util.LinkedList;

import stargatetech2.api.bus.BusPacket;
import stargatetech2.api.bus.IBusDriver;

public class StandardBusDriver implements IBusDriver{
	private LinkedList<BusPacket> queue = new LinkedList();
	private short address;
	
	public StandardBusDriver(short address){
		this.address = address;
	}
	
	@Override public boolean canHandlePacket(short sender, int protocolID, boolean hasLIP){ return false; }
	@Override public void handlePacket(BusPacket packet){}
	@Override public boolean isInterfaceEnabled(){ return true; }
	@Override public short getInterfaceAddress(){ return address; }
	
	@Override
	public BusPacket getNextPacketToSend() {
		return queue.isEmpty() ? null : queue.removeFirst();
	}
	
	public void addpacketToQueue(BusPacket packet){
		queue.addLast(packet);
	}
}