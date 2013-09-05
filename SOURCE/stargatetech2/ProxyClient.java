package stargatetech2;

import stargatetech2.IContentModule.Module;
import stargatetech2.common.base.BaseISBRH;
import stargatetech2.common.machine.RenderBlockMachine;
import stargatetech2.common.util.GUIHandlerClient;
import stargatetech2.core.rendering.RenderNaquadahOre;
import stargatetech2.core.rendering.RenderNaquadahRail;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.network.NetworkRegistry;

public class ProxyClient implements ISidedProxy{

	@Override
	public void registerRenderers(Module module){
		switch(module){
		case CORE:
			registerRenderer(RenderBlockMachine.instance());
			registerRenderer(RenderNaquadahRail.instance());
			registerRenderer(RenderNaquadahOre.instance());
			break;
		case INTEGRATION:
			break;
		}
	}

	@Override
	public void registerGUIHandler() {
		NetworkRegistry.instance().registerGuiHandler(this, new GUIHandlerClient());
	}
	
	private void registerRenderer(BaseISBRH renderer){
		RenderingRegistry.registerBlockHandler(renderer);
	}
}