package stargatetech2.factory;

import stargatetech2.IContentModule;
import stargatetech2.StargateTech2;

public class ModuleFactory implements IContentModule {

	@Override
	public void preInit(){
		
	}

	@Override
	public void init(){
		
	}

	@Override
	public void postInit(){
		StargateTech2.proxy.registerRenderers(Module.FACTORY);
	}

	@Override public void onServerStart(){}
	@Override public void onServerStop(){}

	@Override
	public String getModuleName(){
		return "Factory";
	}
}