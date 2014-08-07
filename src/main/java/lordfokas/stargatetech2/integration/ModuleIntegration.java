package lordfokas.stargatetech2.integration;

import java.util.ArrayList;

import lordfokas.stargatetech2.IContentModule;
import lordfokas.stargatetech2.StargateTech2;
import lordfokas.stargatetech2.core.util.StargateLogger;
import lordfokas.stargatetech2.integration.plugins.BasePlugin;
import lordfokas.stargatetech2.integration.plugins.cc.PluginCC;
import lordfokas.stargatetech2.integration.plugins.ic2.PluginIC2;
import lordfokas.stargatetech2.integration.plugins.te3.PluginTE3;
import lordfokas.stargatetech2.integration.plugins.tico.PluginTiCo;

public class ModuleIntegration implements IContentModule {
	private ArrayList<BasePlugin> plugins = new ArrayList<BasePlugin>(4);
	
	public static BasePlugin tico;
	public static BasePlugin te3;
	public static BasePlugin ic2;
	public static BasePlugin cc;
	
	@Override
	public void preInit(){
		tico = new PluginTiCo();
		te3  = new PluginTE3();
		ic2  = new PluginIC2();
		cc   = new PluginCC();
		
		plugins.add(tico);
		plugins.add(te3);
		plugins.add(ic2);
		plugins.add(cc);
	}

	@Override
	public void init(){
		for(BasePlugin plugin : plugins){
			try{
				StargateLogger.info("Loading Integration Plugin: " + plugin.getModID());
				plugin.init();
			}catch(Exception exception){
				StargateLogger.severe("An error ocurred while loading the Integration Plugin.");
				exception.printStackTrace();
			}
		}
	}

	@Override public void postInit(){
		for(BasePlugin plugin : plugins){
			try{
				StargateLogger.info("Post-Loading Integration Plugin: " + plugin.getModID());
				plugin.postInit();
			}catch(Exception exception){
				StargateLogger.severe("An error ocurred while post-loading the Integration Plugin.");
				exception.printStackTrace();
			}
		}
		StargateTech2.proxy.registerRenderers(Module.INTEGRATION);
	}
	
	@Override public void onServerStart(){}
	@Override public void onServerStop(){}

	@Override
	public String getModuleName(){
		return "Integration";
	}
}