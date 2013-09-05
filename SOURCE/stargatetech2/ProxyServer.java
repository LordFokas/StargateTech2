package stargatetech2;

import stargatetech2.IContentModule.Module;
import stargatetech2.common.util.GUIHandler;
import cpw.mods.fml.common.network.NetworkRegistry;

public class ProxyServer implements ISidedProxy{
	
	/* Unimplemented Client-Side methods. */
	@Override public void registerRenderers(Module module){}

	@Override
	public void registerGUIHandler() {
		NetworkRegistry.instance().registerGuiHandler(this, new GUIHandler());
	}
	
}
