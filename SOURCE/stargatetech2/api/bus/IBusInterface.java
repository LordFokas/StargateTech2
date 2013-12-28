package stargatetech2.api.bus;

/**
 * Do NOT implement this interface. To get an instance use
 * StargateTechAPI.api().getFactory().getIBusInterface();
 * 
 * @author LordFokas
 */
public interface IBusInterface {
	/**
	 * You should call this whenever your IBusDevice TileEntity
	 * adds or removes an IBusInterface from its hardware list.
	 * This method triggers the network remapping routines that
	 * allow other devices to find your IBusDevice. The network
	 * map data is stored in the IBusInterface, which means its
	 * not lost when your TileEntity is unloaded and reloaded.
	 */
	public void updateHardwareState();
	
	/**
	 * Makes the IBusInterface call its IBusDriver's
	 * getNextPacketToSend() method repeatedly until it returns
	 * null. Every packet returned by that method will be sent
	 * across the network.
	 */
	public void sendAllPackets();
}