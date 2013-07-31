package stargatetech2.common.util;

import java.util.HashMap;

import stargatetech2.common.reference.BlockReference;
import stargatetech2.common.reference.ConfigReference;
import net.minecraftforge.common.Configuration;

public class Config {
	private Configuration cfg;
	private HashMap<String, Integer> blocks = new HashMap<String, Integer>();
	private int blockIDs = 1000;
	
	public Config(Configuration cfg){
		this.cfg = cfg;
		cfg.load();
		
		cfg.addCustomCategoryComment(ConfigReference.KEY_CFG_CLIENT, "Client Side options that affect key handling and rendering.");
		cfg.addCustomCategoryComment(ConfigReference.KEY_CFG_SERVER, "Server Side options that affect game logic");
		cfg.addCustomCategoryComment(ConfigReference.KEY_IDS_BLOCKS, "ID Values for blocks.");
		cfg.addCustomCategoryComment(ConfigReference.KEY_IDS_ITEMS, "ID Values for items.");
		
		for(String block : BlockReference.ALL_BLOCKS){
			addBlock(cfg, block);
		}
	}
	
	public void save(){
		cfg.save();
	}
	
	private void addBlock(Configuration cfg, String name){
		blocks.put(name, cfg.get(ConfigReference.KEY_IDS_BLOCKS, name, blockIDs).getInt());
		blockIDs++;
	}
	
	public int getBlockID(String name){
		return blocks.get(name);
	}
}