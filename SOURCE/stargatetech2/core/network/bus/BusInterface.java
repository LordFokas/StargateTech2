package stargatetech2.core.network.bus;

import stargatetech2.api.bus.BusPacket;
import stargatetech2.api.bus.IBusDevice;
import stargatetech2.api.bus.IBusDriver;
import stargatetech2.api.bus.IBusInterface;

public final class BusInterface implements IBusInterface{
	private static final byte BROADCAST		= (byte) 0xFF;
	private static final byte PROMISCUOUS	= (byte) 0x00;
	
	private final IBusDevice device;
	private final IBusDriver driver;
	
	public BusInterface(IBusDevice device, IBusDriver driver){
		this.device = device;
		this.driver = driver;
	}

	@Override
	public void updateHardwareState() {
		// TODO: remap network data.
	}

	@Override
	public void sendAllPackets() {
		if(driver.isInterfaceEnabled()){
			BusPacket packet;
			while((packet = driver.getNextPacketToSend()) != null){
				sendPacket(packet);
			}
		}
	}
	
	private void sendPacket(BusPacket packet){
		// TODO: iterate address list and send packet.
	}
	
	public void recvPacket(BusPacket packet){
		if(!driver.isInterfaceEnabled()) return;
		// split addresses into net and host address.
		// uses /8 netmask in a 16-bit address.
		short address = driver.getInterfaceAddress();
		byte addrNet  = (byte)((address & 0xFF00) >> 8);
		byte addrHost = (byte) (address & 0x00FF);
		short sender  = packet.getSender();
		byte sendNet  = (byte)((sender & 0xFF00) >> 8);
		byte sendHost = (byte) (sender & 0x00FF);
		
		// if addresses match      or it's a broadcast      or we're sniffing
		if(sendNet  == addrNet  || sendNet  == BROADCAST || addrNet  == PROMISCUOUS)
		if(sendHost == addrHost || sendHost == BROADCAST || addrHost == PROMISCUOUS){
			if(driver.canHandlePacket(sender, packet.getProtocolID(), packet.hasPlainText())){
				driver.handlePacket(packet);
			}
		}
	}
}