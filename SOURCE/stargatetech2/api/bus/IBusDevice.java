package stargatetech2.api.bus;

/**
 * To be implemented by Tile Entities that wish
 * to access the Abstract Bus.
 * 
 * @author LordFokas
 */
public interface IBusDevice {
	/**
	 * Returns the IBusInterfaces that exist on that
	 * side of the Tile Entity. It may be multiple
	 * values or null.
	 * 
	 * @param side The side of the block that is being queried.
	 * @return This side's IBusInterface, if any.
	 */
	public IBusInterface[] getInterfaces(int side);
}
