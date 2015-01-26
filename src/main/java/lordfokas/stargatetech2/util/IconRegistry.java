package lordfokas.stargatetech2.util;

import java.util.HashMap;

import lordfokas.stargatetech2.modules.enemy.IonizedParticles;
import lordfokas.stargatetech2.modules.integration.tico.MoltenNaquadah;
import lordfokas.stargatetech2.reference.ModReference;
import lordfokas.stargatetech2.reference.TextureReference;
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
