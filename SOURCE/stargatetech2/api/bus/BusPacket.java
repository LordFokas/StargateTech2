package stargatetech2.api.bus;

public abstract class BusPacket {
	private final short sender;
	private final short target;
	
	/**
	 * @param sender The address of the Device that is sending this packet.
	 * @param target The address of the Device(s) that should receive this packet.
	 */
	protected BusPacket(short sender, short target){
		this.sender = sender;
		this.target = target;
	}
	
	/**
	 * @return The address of the device that sent this packet.
	 */
	public final short getSender(){
		return sender;
	}
	
	/**
	 * @return The address of the device(s) that should receive this packet.
	 */
	public final short getTarget(){
		return target;
	}
	
	/**
	 * @return The ID of the protocol this packet corresponds to.
	 */
	public final int getProtocolID(){
		return BusProtocols.getProtocolID(this.getClass());
	}
}
