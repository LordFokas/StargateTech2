package stargatetech2.integration;

import java.util.ArrayList;

import stargatetech2.IContentModule;
import stargatetech2.StargateTech2;
import stargatetech2.core.util.StargateLogger;
import stargatetech2.integration.plugins.BasePlugin;
import stargatetech2.integration.plugins.cc.PluginCC;
import stargatetech2.integration.plugins.ic2.PluginIC2;
import stargatetech2.integration.plugins.te3.PluginTE3;

public class ModuleIntegration implements IContentModule {
	private ArrayList<BasePlugin> plugins = new ArrayList<BasePlugin>();
	
	public static BasePlugin te3;
	public static BasePlugin ic2;
	public static BasePlugin cc;
	
	@Override
	public void preInit(){
		te3	= new PluginTE3();
		ic2	= new PluginIC2();
		cc	= new PluginCC();
		
		plugins.add(te3);
		plugins.add(ic2);
		plugins.add(cc);
	}

	@Override
	public void init(){
		for(BasePlugin plugin : plugins){
			try{
				StargateLogger.info("Loading Integration Plugin: " + plugin.getModID());
				plugin.run();
			}catch(Exception exception){
				StargateLogger.severe("An error ocurred while loading the Integration Plugin.");
				exception.printStackTrace();
			}
		}
	}

	@Override public void postInit(){
		StargateTech2.proxy.registerRenderers(Module.INTEGRATION);
	}
	
	@Override public void onServerStart(){}
	@Override public void onServerStop(){}

	@Override
	public String getModuleName(){
		return "Integration";
	}
}