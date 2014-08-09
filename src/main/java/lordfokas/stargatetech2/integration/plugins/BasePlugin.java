package lordfokas.stargatetech2.integration.plugins;

import net.minecraftforge.common.config.Configuration;
import lordfokas.stargatetech2.StargateTech2;
import lordfokas.stargatetech2.core.reference.ConfigReference;
import cpw.mods.fml.common.Loader;

public abstract class BasePlugin {
	protected final String cfgKey;
	protected String modID;
	protected Configuration cfg;
	private boolean enabled;
	
	public BasePlugin(String modID, String cfgKey){
		this.modID = modID;
		this.cfgKey = cfgKey;
		this.cfg = StargateTech2.config.cfg;
		this.enabled = cfg.get(cfgKey, ConfigReference.PLUGIN_ENABLE, true).getBoolean(true);
	}
	
	public final boolean shouldLoad(){
		return(Loader.isModLoaded(modID) && enabled);
	}
	
	public final String getModID(){
		return modID;
	}
	
	public final void init(){
		if(shouldLoad()){
			load();
		}else{
			fallback();
		}
	}
	
	public final void postInit(){
		if(shouldLoad()){
			postLoad();
		}
	}
	
	protected abstract void load();
	protected abstract void postLoad();
	protected abstract void fallback();
}