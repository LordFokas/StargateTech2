package lordfokas.stargatetech2.core.util;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import lordfokas.stargatetech2.core.reference.ConfigReference;

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
		cfg.addCustomCategoryComment(ConfigReference.KEY_IDS_BLOCKS, "ID Values for blocks.");
		cfg.addCustomCategoryComment(ConfigReference.KEY_IDS_ITEMS, "ID Values for items.");
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
	
	/*
	public int getBlockID(String name){
		while(Block.blocksList[blockIDs] != null){
			blockIDs++;
		}
		return cfg.get(ConfigReference.KEY_IDS_BLOCKS, name, blockIDs).getInt();
	}
	
	public int getItemID(String name){
		while(Item.itemsList[itemIDs + 256] != null){
			itemIDs++;
		}
		return cfg.get(ConfigReference.KEY_IDS_ITEMS, name, itemIDs).getInt();
	}
	*/
	
	public void setServerConfigs(){
		// BASE CONFIGS
		int range = cfg.get(ConfigReference.KEY_CFG_SERVER, "shieldEmitterRange", 5).getInt();
		if(range < 3) range = 3;
		if(range > 12)range = 12;
		ConfigServer.shieldEmitterRange = range;
		cfg.getCategory(ConfigReference.KEY_CFG_SERVER).get("shieldEmitterRange").set(range);
		
		int minDistance = cfg.get(ConfigReference.KEY_CFG_SERVER, "stargateMinDistance", 150).getInt();
		if(minDistance < 50) minDistance = 50;
		cfg.getCategory(ConfigReference.KEY_CFG_SERVER).get("stargateMinDistance").set(minDistance);
		ConfigServer.stargateMinDistance = minDistance * minDistance;
		
		// WORLDGEN CONFIGS
		int podGap = cfg.get(ConfigReference.KEY_CFG_SV_WGEN, "lootPodSpacing", 6).getInt();
		ConfigServer.wgLootPodGap = podGap < 6 ? 6 : podGap;
		cfg.getCategory(ConfigReference.KEY_CFG_SV_WGEN).get("lootPodSpacing").set(ConfigServer.wgLootPodGap);
		int podOdd = cfg.get(ConfigReference.KEY_CFG_SV_WGEN, "lootPodChance", 10).getInt();
		ConfigServer.wgLootPodOdd = podOdd < 5 ? 5 : podOdd;
		cfg.getCategory(ConfigReference.KEY_CFG_SV_WGEN).get("lootPodChance").set(ConfigServer.wgLootPodOdd);
	}
	
	public void setClientConfigs(){}
}