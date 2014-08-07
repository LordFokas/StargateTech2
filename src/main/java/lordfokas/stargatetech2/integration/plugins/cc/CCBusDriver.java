package lordfokas.stargatetech2.integration.plugins.cc;

import lordfokas.stargatetech2.api.bus.BusPacket;
import lordfokas.stargatetech2.api.bus.IBusDriver;

public class CCBusDriver implements IBusDriver {
	private TileBusAdapter busAdapter;
	private boolean isEnabled;
	private short address;
	
	public CCBusDriver(TileBusAdapter busAdapter){
		this.busAdapter = busAdapter;
		this.setEnabled(false);
		this.address = (short) 0xFFFF;
	}
	
	public void setEnabled(boolean isEnabled){
		this.isEnabled = isEnabled;
	}
	
	public void setInterfaceAddress(short address){
		this.address = address;
	}
	
	@Override
	public boolean canHandlePacket(short sender, int protocolID, boolean hasLIP) {
		return hasLIP;
	}
	
	@Override
	public void handlePacket(BusPacket packet) {
		busAdapter.handlePacket(packet.getPlainText());
	}
	
	@Override
	public BusPacket getNextPacketToSend() {
		return busAdapter.getNextPacket();
	}
	
	@Override
	public boolean isInterfaceEnabled() {
		return isEnabled;
	}
	
	@Override
	public short getInterfaceAddress() {
		return address;
	}

	@Override
	public String getShortName() {
		return "Abstract Bus Adapter";
	}

	@Override
	public String getDescription() {
		return "The Abstract Bus Adapter can be used by ComputerCraft computers to interact with the Abstract Bus.";
	}
}