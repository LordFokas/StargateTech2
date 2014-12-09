package lordfokas.stargatetech2.integration;

import java.util.LinkedList;
import java.util.List;

import lordfokas.stargatetech2.IContentModule;
import lordfokas.stargatetech2.StargateTech2;
import lordfokas.stargatetech2.core.reference.ConfigReference;
import lordfokas.stargatetech2.core.util.StargateLogger;
import lordfokas.stargatetech2.integration.plugins.PluginProxy;

public class ModuleIntegration implements IContentModule {
	private List<PluginProxy> proxies = new LinkedList<PluginProxy>();
	
	public static PluginProxy tico;
	public static PluginProxy te3;
	public static PluginProxy ic2;
	public static PluginProxy cc;
	
	@Override
	public void preInit(){
		tico = new PluginProxy("TConstruct", ConfigReference.KEY_PLUGINS_TICO, "tico.PluginTiCo");
		te3  = new PluginProxy("ThermalExpansion", ConfigReference.KEY_PLUGINS_TE3, "te3.PluginTE3");
		ic2  = new PluginProxy("IC2", ConfigReference.KEY_PLUGINS_IC2, "ic2.PluginIC2");
		cc   = new PluginProxy("ComputerCraft", ConfigReference.KEY_PLUGINS_CC, "cc.PluginCC");
		
		proxies.add(tico);
		proxies.add(te3);
		proxies.add(ic2);
		proxies.add(cc);
	}

	@Override
	public void init(){
		for(PluginProxy plugin : proxies){
			try{
				String action = plugin.shouldLoad() ? "Loading" : "Skipping";
				StargateLogger.info(action + " Integration Plugin: " + plugin.getModID());
				plugin.init();
			}catch(Exception exception){
				StargateLogger.error("An error ocurred while loading the Integration Plugin.");
				exception.printStackTrace();
			}
		}
	}

	@Override public void postInit(){
		for(PluginProxy plugin : proxies){
			try{
				if(plugin.shouldLoad()){
					StargateLogger.info("Post-Loading Integration Plugin: " + plugin.getModID());
					plugin.postInit();
				}
			}catch(Exception exception){
				StargateLogger.error("An error ocurred while post-loading the Integration Plugin.");
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