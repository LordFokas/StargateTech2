package lordfokas.stargatetech2.modules.integration;

import lordfokas.stargatetech2.StargateTech2;
import lordfokas.stargatetech2.reference.ConfigReference;
import lordfokas.stargatetech2.util.StargateLogger;
import net.minecraftforge.common.config.Configuration;

public final class PluginProxy<Plugin extends IPlugin>{
	private static final String PACKAGE = PluginProxy.class.getPackage().getName() + ".";
	
	protected final String cfgKey;
	protected final String modID;
	protected final Configuration cfg;
	private final boolean enabled, modPresent;
	private final String main, fallback;
	private final Plugin plugin;
	
	public enum Stage{
		LOAD, POSTLOAD
	}
	
	public PluginProxy(String modID, String cfgKey, String main){
		this(modID, cfgKey, main, null);
	}
	
	public PluginProxy(String modID, String cfgKey, String main, String fallback){
		this.modID = modID;
		this.cfgKey = cfgKey;
		this.cfg = StargateTech2.config.cfg;
		this.enabled = cfg.get(cfgKey, ConfigReference.PLUGIN_ENABLE, true).getBoolean(true);
		this.modPresent = false; // Loader.isModLoaded(modID);
		this.main = PACKAGE + main;
		if(fallback != null){
			this.fallback = PACKAGE + fallback;
		}else{
			this.fallback = null;
		}
		this.plugin = getPluginInstance();
	}
	
	public Plugin getPlugin(){
		return plugin;
	}
	
	public boolean shouldLoad(){
		return modPresent && enabled;
	}
	
	public String getModID(){
		return modID;
	}
	
	public void     init(){ run(Stage.LOAD);     }
	public void postInit(){ run(Stage.POSTLOAD); }
	
	private Plugin getPluginInstance(){
		Plugin plugin = null;
		String className = shouldLoad() ? main : fallback;
		if(className != null){
			try{
				Class cls = Class.forName(className);
				Object obj = cls.newInstance();
				if(obj instanceof IPlugin){
					plugin = (Plugin) obj;
				}else{
					StargateLogger.warning("Class " + className + "is not a valid IPlugin");
				}
			}catch(Exception e){
				StargateLogger.error("Error instancing plugin class: " + className);
				e.printStackTrace();
			}
		}
		return plugin;
	}
	
	private void run(Stage stage){
		if(plugin == null) return;
		switch(stage){
			case LOAD:
				plugin.load();
				break;
			case POSTLOAD:
				plugin.postload();
				break;
		}
	}
}