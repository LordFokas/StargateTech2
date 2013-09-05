package stargatetech2;

import stargatetech2.IContentModule.Module;

public interface ISidedProxy {
	public void registerRenderers(Module module);
	public void registerGUIHandler();
}
