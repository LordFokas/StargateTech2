package lordfokas.stargatetech2.modules;

import java.util.LinkedList;
import java.util.List;

import lordfokas.stargatetech2.StargateTech2;
import lordfokas.stargatetech2.modules.integration.PluginProxy;
import lordfokas.stargatetech2.modules.integration.cc.PluginCC;
import lordfokas.stargatetech2.modules.integration.ic2.PluginIC2;
import lordfokas.stargatetech2.modules.integration.oc.PluginOC;
import lordfokas.stargatetech2.modules.integration.te4.PluginTE4;
import lordfokas.stargatetech2.modules.integration.tico.PluginTiCo;
import lordfokas.stargatetech2.modules.integration.waila.PluginWaila;
import lordfokas.stargatetech2.reference.ConfigReference;
import lordfokas.stargatetech2.util.StargateLogger;

public class ModuleIntegration implements IContentModule {
	private List<PluginProxy> proxies = new LinkedList<PluginProxy>();
	
	public static PluginProxy<PluginWaila> waila;
	public static PluginProxy<PluginTiCo> tico;
	public static PluginProxy<PluginTE4> te4;
	public static PluginProxy<PluginOC> oc;
	public static PluginProxy<PluginIC2> ic2;
	public static PluginProxy<PluginCC> cc;
	
	@Override
	public void preInit(){
		waila	= new PluginProxy("Waila", ConfigReference.KEY_PLUGINS_WAILA, "waila.PluginWaila");
		tico	= new PluginProxy("TConstruct", ConfigReference.KEY_PLUGINS_TICO, "tico.PluginTiCo");
		te4		= new PluginProxy("ThermalExpansion", ConfigReference.KEY_PLUGINS_TE4, "te4.PluginTE4");
		oc		= new PluginProxy("OpenComputers", ConfigReference.KEY_PLUGINS_OC, "oc.PluginOC");
		ic2		= new PluginProxy("IC2", ConfigReference.KEY_PLUGINS_IC2, "ic2.PluginIC2");
		cc		= new PluginProxy("ComputerCraft", ConfigReference.KEY_PLUGINS_CC, "cc.PluginCC");
		
		proxies.add(waila);
		proxies.add(tico);
		proxies.add(te4);
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