package lordfokas.stargatetech2.integration.waila;

import lordfokas.stargatetech2.api.stargate.ITileStargate;
import lordfokas.stargatetech2.integration.IPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import cpw.mods.fml.common.event.FMLInterModComms;

public class PluginWaila implements IPlugin{
	
	@Override
	public void load() {
		FMLInterModComms.sendMessage("Waila", "register", PluginWaila.class.getName() + ".register");
	}
	
	@Override
	public void postload(){}
	
	public static void register(IWailaRegistrar waila){
		waila.registerBodyProvider(new StargateDataProvider(), ITileStargate.class);
		
		// TODO: get key sync working first.
		// waila.registerBodyProvider(new CommonDataProvider(), BaseTileEntity.class);
	}
}