package stargatetech2.common.util;

import java.util.HashMap;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.Icon;
import stargatetech2.common.reference.ModReference;
import stargatetech2.common.reference.TextureReference;
import stargatetech2.core.util.IonizedParticles;

public class IconRegistry {
	public static final HashMap<String, Icon> blockIcons = new HashMap<String, Icon>();
	public static final HashMap<String, Icon> itemIcons = new HashMap<String, Icon>();
	
	public static void load(TextureMap map){
		if(map.textureType == 0){
			loadTerrain(map);
		} else {
			loadItems(map);
		}
	}
	
	private static void loadTerrain(TextureMap map){
		for(String textureName : TextureReference.TERRAIN_TEXTURES){
			load(textureName, map, blockIcons);
		}
		IonizedParticles.fluid.setIcons(blockIcons.get(TextureReference.IONIZED_PARTICLES));
	}
	
	private static void loadItems(TextureMap map){
		for(String textureName : TextureReference.ITEM_TEXTURES){
			load(textureName, map, itemIcons);
		}
	}
	
	private static void load(String texture, TextureMap map, HashMap<String, Icon> iconList){
		iconList.put(texture, map.registerIcon(ModReference.MOD_ID + ":" + texture));
	}
}
