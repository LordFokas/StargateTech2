package lordfokas.stargatetech2.core;

import java.util.HashMap;

import lordfokas.stargatetech2.core.reference.ModReference;
import lordfokas.stargatetech2.core.reference.TextureReference;
import lordfokas.stargatetech2.enemy.IonizedParticles;
import lordfokas.stargatetech2.integration.tico.MoltenNaquadah;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;

public class IconRegistry {
	public static final HashMap<String, IIcon> blockIcons = new HashMap<String, IIcon>();
	public static final HashMap<String, IIcon> itemIcons = new HashMap<String, IIcon>();
	
	public static void load(TextureMap map){
		if(map.getTextureType() == 0){
			loadTerrain(map);
		} else if(map.getTextureType() == 1) {
			loadItems(map);
		}
	}
	
	private static void loadTerrain(TextureMap map){
		for(String textureName : TextureReference.TERRAIN_TEXTURES){
			load(textureName, map, blockIcons);
		}
		IonizedParticles.fluid.setIcons(blockIcons.get(TextureReference.IONIZED_PARTICLES));
		MoltenNaquadah.instance.setIcons(blockIcons.get(TextureReference.MOLTEN_NAQUADAH), blockIcons.get(TextureReference.MOLTEN_NAQUADAH_FLOW));
	}
	
	private static void loadItems(TextureMap map){
		for(String textureName : TextureReference.ITEM_TEXTURES){
			load(textureName, map, itemIcons);
		}
	}
	
	private static void load(String texture, TextureMap map, HashMap<String, IIcon> iconList){
		iconList.put(texture, map.registerIcon(ModReference.MOD_ID + ":" + texture));
	}
}
