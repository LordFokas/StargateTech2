package lordfokas.stargatetech2.modules.transport.stargates;

import lordfokas.stargatetech2.api.stargate.Address;
import lordfokas.stargatetech2.api.stargate.Symbol;

public class LoreAddresses {
	// Addresses
	public static final Address EARTH	= StargateNetwork.parse("Sileston Salarta Om");
	public static final Address TOLLAN	= StargateNetwork.parse("Dehsetcro Tisarus At");
	public static final Address ABYDOS	= StargateNetwork.parse("Croecom Erpvabrei At");
	
	// Prefixes
	public static final Symbol[] OVERWORLD	= new Symbol[]{Symbol.SIL, Symbol.EST, Symbol.ON};
	public static final Symbol[] NETHER		= new Symbol[]{Symbol.DEH, Symbol.SET, Symbol.CRO};
	public static final Symbol[] ATUM		= new Symbol[]{Symbol.CRO, Symbol.EC, Symbol.OM};
}