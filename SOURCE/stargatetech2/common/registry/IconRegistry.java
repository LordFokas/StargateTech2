package stargatetech2.common.registry;

import java.util.HashMap;

import stargatetech2.common.reference.ModReference;
import stargatetech2.common.reference.TextureReference;
import stargatetech2.core.IonizedParticles;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.Icon;

public class IconRegistry {
	public static final HashMap<String, Icon> icons = new HashMap<String, Icon>();
	
	public static void load(TextureMap map){
		if(map.textureType == 0) loadTerrain(map);
		else loadItems(map);
	}
	
	private static void loadTerrain(TextureMap map){
		load(TextureReference.IONIZED_PARTICLES, map);
		IonizedParticles.fluid.setIcons(icons.get(TextureReference.IONIZED_PARTICLES));
	}
	
	private static void loadItems(TextureMap map){
		
	}
	
	private static void load(String texture, TextureMap map){
		icons.put(texture, map.registerIcon(ModReference.MOD_ID + ":" + texture));
	}
}
