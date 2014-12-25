package lordfokas.stargatetech2.integration.waila;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInterModComms;
import lordfokas.stargatetech2.integration.IPlugin;

public class PluginWaila implements IPlugin{

	@Override
	public void load() {
		FMLInterModComms.sendMessage("Waila", "register", WailaEntryPoint.class.getName() + ".callback");
	}

	@Override
	public void postload(){}
}
