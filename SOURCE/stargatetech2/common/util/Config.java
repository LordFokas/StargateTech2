package stargatetech2.common.util;

import java.util.HashMap;
import java.util.Map;

import stargatetech2.common.reference.BlockReference;
import stargatetech2.common.reference.ConfigReference;
import stargatetech2.common.reference.ItemReference;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.ConfigCategory;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

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
		cfg.addCustomCategoryComment(ConfigReference.KEY_CFG_SERVER, "Server Side options that affect game logic");
		cfg.addCustomCategoryComment(ConfigReference.KEY_IDS_BLOCKS, "ID Values for blocks.");
		cfg.addCustomCategoryComment(ConfigReference.KEY_IDS_ITEMS, "ID Values for items.");
		cfg.addCustomCategoryComment(ConfigReference.KEY_PLUGINS, "Configuration values for Integration Plugins.");
		
		for(String plugin : ConfigReference.PLUGIN_LIST) addPlugin(cfg, plugin);
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
}