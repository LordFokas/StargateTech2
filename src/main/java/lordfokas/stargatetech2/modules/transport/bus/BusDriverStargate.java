package lordfokas.stargatetech2.modules.transport.bus;

import java.util.LinkedList;

import lordfokas.stargatetech2.api.bus.BusPacket;
import lordfokas.stargatetech2.api.bus.BusPacketLIP;
import lordfokas.stargatetech2.api.bus.BusProtocols;
import lordfokas.stargatetech2.api.bus.IBusDriver;
import lordfokas.stargatetech2.api.stargate.Address;
import lordfokas.stargatetech2.api.stargate.DialError;
import lordfokas.stargatetech2.api.stargate.ITileStargateBase.DialMethod;
import lordfokas.stargatetech2.modules.transport.TileStargate;
import lordfokas.stargatetech2.modules.transport.stargates.StargateNetwork;

public class BusDriverStargate implements IBusDriver{
	private LinkedList<BusPacket> queue = new LinkedList();
	private TileStargate stargate;
	
	public BusDriverStargate(TileStargate stargate){
		this.stargate = stargate;
	}
	
	@Override
	public boolean canHandlePacket(short sender, int protocolID, boolean hasLIP) {
		return protocolID == BusProtocols.PROTOCOL_LIP;
	}

	@Override
	public void handlePacket(BusPacket packet) {
		BusPacketLIP lip = packet.getPlainText();
		String action = lip.get("action");
		if(action != null){
			if(action.equalsIgnoreCase("disconnect")){
				stargate.disconnect();
				lip.addResponse("Disconnecting Wormhole");
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
						DialError error = stargate.dial(address, timeout, DialMethod.MANUAL);
						if(error == DialError.SUCCESSFULLY_DIALED){
							lip.addResponse(String.format("Dialing %s for %d seconds", address.toString(), timeout));
						}else{
							lip.addResponse("Dialing error: " + error);
						}
					}else{
						lip.addResponse("ERROR: Address Invalid!");
					}
				}else{
					lip.addResponse("ERROR: you must provide an address!");
				}
			}else if(action.equalsIgnoreCase("openIris")){
				// stargate.openIris();
				lip.addResponse("Open what now?");
			}else if(action.equalsIgnoreCase("closeIris")){
				// stargate.closeIris();
				lip.addResponse("Who is that Irish you speak of?");
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

	@Override
	public String getShortName() {
		return "Stargate";
	}

	@Override
	public String getDescription() {
		return "The Stargate is an extremely advanced portal that uses a Wormhole to teleport entities to other worlds.";
	}
}