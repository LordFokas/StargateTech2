package stargatetech2.common.util;

import java.util.HashMap;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import stargatetech2.common.reference.ModReference;
import stargatetech2.common.reference.TextureReference;

public class IconRegistry {
	private static HashMap<String, Icon> blocks = new HashMap<String, Icon>();
	//private static HashMap<String, Icon>  items = new HashMap<String, Icon>();
	
	public static void loadBlocks(IconRegister register){
		for(String texture : TextureReference.BLOCKS){
			blocks.put(texture, register.registerIcon(ModReference.MOD_ID + ":" + texture));
		}
	}
	
	public static Icon getBlockIcon(String name){
		return blocks.get(name);
	}
	
	/*
	public static void loadItems(IconRegister register){
		for(String texture : TextureReference.BLOCK_TEXTURES){
			items.put(texture, register.registerIcon(ModReference.MOD_ID + ":" + texture));
		}
	}
	
	public static Icon getItemIcon(String name){
		return items.get(name);
	}
	*/
}