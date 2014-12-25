package lordfokas.stargatetech2.integration.waila;

import lordfokas.stargatetech2.api.stargate.ITileStargate;
import mcp.mobius.waila.api.IWailaRegistrar;

public class WailaEntryPoint {
	public static void callback(IWailaRegistrar waila){
		waila.registerBodyProvider(new StargateDataProvider(), ITileStargate.class);
	}
}
