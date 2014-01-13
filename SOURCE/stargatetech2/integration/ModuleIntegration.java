package stargatetech2.integration;

import java.util.ArrayList;

import stargatetech2.IContentModule;
import stargatetech2.integration.plugins.BasePlugin;
import stargatetech2.integration.plugins.cc.PluginCC;
import stargatetech2.integration.plugins.ic2.PluginIC2;

public class ModuleIntegration implements IContentModule {
	private ArrayList<BasePlugin> plugins = new ArrayList<BasePlugin>();
	
	@Override public void preInit(){}

	@Override
	public void init(){
		addPlugin(new PluginIC2());
		addPlugin(new PluginCC());
	}

	@Override
	public void postInit(){
		for(BasePlugin plugin : plugins){
			plugin.load();
		}
	}

	@Override public void onServerStart(){}
	@Override public void onServerStop(){}

	@Override
	public String getModuleName(){
		return "Integration";
	}
	
	private void addPlugin(BasePlugin plugin){
		if(plugin.shouldLoad())
			plugins.add(plugin);
	}
}
