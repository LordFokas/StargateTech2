package stargatetech2.transport.bus;

import java.util.LinkedList;

import stargatetech2.api.bus.BusPacket;
import stargatetech2.api.bus.BusPacketLIP;
import stargatetech2.api.bus.IBusDriver;
import stargatetech2.api.stargate.Address;
import stargatetech2.transport.stargates.StargateNetwork;
import stargatetech2.transport.tileentity.TileStargate;

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
		BusPacketLIP lip = packet.getPlainText();
		String action = lip.get("action");
		if(action != null){
			if(action.equalsIgnoreCase("disconnect")){
				stargate.disconnect();
			}else if(action.equalsIgnoreCase("dial")){
				String addr = lip.get("address");
				if(addr != null){
					int timeout;
					try{
						timeout = Integer.parseInt(lip.get("timeout"));
					}catch(Exception e){
						timeout = 38;
					}
					Address address = StargateNetwork.parse(addr);
					if(address != null){
						stargate.dial(address, timeout);
					}
				}
			}else if(action.equalsIgnoreCase("openIris")){
				stargate.openIris();
			}else if(action.equalsIgnoreCase("closeIris")){
				stargate.closeIris();
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