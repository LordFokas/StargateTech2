package lordfokas.stargatetech2.core;

import java.util.HashMap;

import lordfokas.stargatetech2.core.reference.ConfigReference;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

public class Config {
	public Configuration cfg;
	private HashMap<String, PluginConfig> plugins = new HashMap<String, PluginConfig>();
	private int blockIDs = 1000;
	private int itemIDs  = 5000;
	
	public static class PluginConfig{
		private ConfigCategory category;
		
		public PluginConfig(ConfigCategory category){
			this.category = category;
		}
		
		public ConfigCategory getConfig(){
			return category;
		}
	}
	
	public Config(Configuration cfg){
		this.cfg = cfg;
		cfg.load();
		
		cfg.addCustomCategoryComment(ConfigReference.KEY_CFG_CLIENT, "Client Side options that affect key handling and rendering.");
		cfg.addCustomCategoryComment(ConfigReference.KEY_CFG_SERVER, "Server Side options that affect game logic.");
		cfg.addCustomCategoryComment(ConfigReference.KEY_CFG_SV_WGEN, "World Generation settings.");
		cfg.addCustomCategoryComment(ConfigReference.KEY_PLUGINS, "Configuration values for Integration Plugins.");
		
		for(String plugin : ConfigReference.PLUGIN_LIST) addPlugin(cfg, plugin);
		setServerConfigs();
		setClientConfigs();
	}
	
	public void save(){
		cfg.save();
	}
	
	private void addPlugin(Configuration cfg, String plugin){
		ConfigCategory category = cfg.getCategory(plugin);
		plugins.put(plugin, new PluginConfig(category));
	}
	
	public ConfigCategory getPluginConfig(String key){
		PluginConfig pc = plugins.get(key);
		if(pc == null) return null;
		else return pc.getConfig();
	}
	
	public void setServerConfigs(){
		// BASE CONFIGS
		ConfigServer.shieldEmitterRange = cfg.getInt("shieldEmitterRange", ConfigReference.KEY_CFG_SERVER, ConfigServer.shieldEmitterRange, 3, 12, "The maximum gap between Shield Emitter pairs.");
		int minDistance = cfg.getInt("stargateMinDistance", ConfigReference.KEY_CFG_SERVER, 150, 50, 100000000, "The minimum diagonal distance, in blocks, between two stargates.");
		ConfigServer.stargateMinDistance = minDistance * minDistance;
		
		// WORLDGEN CONFIGS
		ConfigServer.wgLootPodGap = cfg.getInt("lootPodSpacing", ConfigReference.KEY_CFG_SV_WGEN, ConfigServer.wgLootPodGap, 6, 100, "The minimum distance, in chunks, between two Loot Pods.");
		ConfigServer.wgLootPodOdd = cfg.getInt("lootPodRarity", ConfigReference.KEY_CFG_SV_WGEN, ConfigServer.wgLootPodOdd, 0, 100, "Loot Pod Rarity. 0 = Don't generate Loot Pods.");
		ConfigServer.wgNaquadah = cfg.getInt("naquadahRarity", ConfigReference.KEY_CFG_SV_WGEN, ConfigServer.wgNaquadah, 0, 2500, "Naquadah Rarity. 0 = Don't generate Naquadah.");
	}
	
	public void setClientConfigs(){
		String comment = "If enabled it will display a green overlay on your screen when you walk over a Transport Ring platform.";
		ConfigClient.enableRingGUIOverlay = cfg.getBoolean("enableRingGUIOverlay", ConfigReference.KEY_CFG_CLIENT, ConfigClient.enableRingGUIOverlay, comment);
	}
}