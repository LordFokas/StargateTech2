package stargatetech2.api.stargate;

public interface IStargateNetwork {
	/**
	 * @return Whether the Stargate Network is loaded (working) or not.
	 */
	public boolean isLoaded();
	
	/**
	 * @param address The string representation of an address. (e.g. "Proclarush Taonas At")
	 * @return an address object if the string is a valid address, null otherwise.
	 */
	public Address parseAddress(String address);
	
	/**
	 * Checks if a given address exists in the network or not.
	 * (i.e., if this address maps to a physical Stargate)
	 * 
	 * @param address the address we want to check.
	 * @return whether the address exists or not.
	 */
	public boolean addressExists(Address address);
}
