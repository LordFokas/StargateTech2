package lordfokas.stargatetech2.integration.plugins;

import net.minecraftforge.common.config.Configuration;
import lordfokas.stargatetech2.StargateTech2;
import lordfokas.stargatetech2.core.reference.ConfigReference;
import lordfokas.stargatetech2.core.util.StargateLogger;
import cpw.mods.fml.common.Loader;

public final class PluginProxy {
	private static final String PACKAGE = PluginProxy.class.getPackage().getName() + ".";
	
	protected final String cfgKey;
	protected String modID;
	protected Configuration cfg;
	private boolean enabled;
	private String concrete;
	
	public enum Callback{
		LOAD, POSTLOAD, FALLBACK
	}
	
	public PluginProxy(String modID, String cfgKey, String concrete){
		this.modID = modID;
		this.cfgKey = cfgKey;
		this.cfg = StargateTech2.config.cfg;
		this.enabled = cfg.get(cfgKey, ConfigReference.PLUGIN_ENABLE, true).getBoolean(true);
		this.concrete = PACKAGE + concrete;
		
	}
	
	public final boolean shouldLoad(){
		return(Loader.isModLoaded(modID) && enabled);
	}
	
	public final String getModID(){
		return modID;
	}
	
	public final void init(){
		if(shouldLoad()){
			run(Callback.LOAD);
		}else{
			run(Callback.FALLBACK);
		}
	}
	
	public final void postInit(){
		if(shouldLoad()){
			run(Callback.POSTLOAD);
		}
	}
	
	private void run(Callback callback){
		try{
			if(concrete == null || concrete.isEmpty()) return;
			Class cls = Class.forName(concrete);
			Object obj = cls.newInstance();
			if(obj instanceof IPlugin){
				switch(callback){
				case FALLBACK:
					((IPlugin)obj).fallback();
					break;
				case LOAD:
					((IPlugin)obj).load();
					break;
				case POSTLOAD:
					((IPlugin)obj).postload();
					break;
				}
			}else{
				StargateLogger.warning("Provided class isn't IPlugin: " + concrete);
			}
		}catch(Exception e){
			StargateLogger.error("Error running IPlugin callback: " + concrete);
			e.printStackTrace();
		}
		
	}
}