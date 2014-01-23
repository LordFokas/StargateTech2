package stargatetech2.integration;

import java.util.ArrayList;

import stargatetech2.IContentModule;
import stargatetech2.integration.plugins.BasePlugin;
import stargatetech2.integration.plugins.cc.PluginCC;
import stargatetech2.integration.plugins.ic2.PluginIC2;
import stargatetech2.integration.plugins.te3.PluginTE3;

public class ModuleIntegration implements IContentModule {
	public static boolean naqDustRecipeAdded = false;
	
	private ArrayList<BasePlugin> plugins = new ArrayList<BasePlugin>();
	
	@Override
	public void preInit(){
		addPlugin(new PluginTE3());
		addPlugin(new PluginIC2());
		addPlugin(new PluginCC());
	}

	@Override
	public void init(){
		for(BasePlugin plugin : plugins){
			plugin.load();
		}
	}

	@Override public void postInit(){}
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
