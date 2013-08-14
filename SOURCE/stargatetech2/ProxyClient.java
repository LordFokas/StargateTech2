package stargatetech2;

import stargatetech2.common.base.BaseISBRH;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ProxyClient implements ISidedProxy{

	@Override
	public void registerRenderer(BaseISBRH renderer) {
		RenderingRegistry.registerBlockHandler(renderer);
	}
	
}