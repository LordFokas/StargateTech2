package stargatetech2.core.reference;

import java.util.ArrayList;

public class ConfigReference {
	
	//****************************************************************************************************
	// TREE NODES
	public static final String KEY_CFG_SERVER		= "cfg.server";
	public static final String KEY_CFG_SV_WGEN		= "cfg.server.worldgen";
	public static final String KEY_CFG_CLIENT		= "cfg.client";
	public static final String KEY_IDS_BLOCKS		= "ids.blocks";
	public static final String KEY_IDS_ITEMS		= "ids.items";
	public static final String KEY_PLUGINS			= "plugins";
	public static final String KEY_PLUGINS_CC		= "plugins.computercraft";
	public static final String KEY_PLUGINS_FORESTRY	= "plugins.forestry";
	public static final String KEY_PLUGINS_IC2		= "plugins.industrialcraft2";
	public static final String KEY_PLUGINS_TE3		= "plugins.thermalexpansion3";
	
	
	//****************************************************************************************************
	// SERVER
	
	
	//****************************************************************************************************
	// CLIENT
	
	
	//****************************************************************************************************
	// PLUGINS
	public static final ArrayList<String> PLUGIN_LIST = new ArrayList<String>();
	
	public static final String PLUGIN_ENABLE = "enable";
	
	
	
	static{
		PLUGIN_LIST.add(KEY_PLUGINS_IC2);
	}
}