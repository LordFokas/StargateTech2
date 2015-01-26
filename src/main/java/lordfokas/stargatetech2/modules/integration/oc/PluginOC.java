package lordfokas.stargatetech2.modules.integration.oc;

import lordfokas.stargatetech2.modules.integration.IPlugin;
import lordfokas.stargatetech2.modules.transport.TileStargate;

public class PluginOC implements IPlugin{
	
	// nothing to do here, yet.
	@Override public void load(){}
	@Override public void postload(){}
	
	public void redirectWirelessPacket(TileStargate target, Object packet){
		// TODO: handle stuff here.
	}
}