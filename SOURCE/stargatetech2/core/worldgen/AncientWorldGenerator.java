package stargatetech2.core.worldgen;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

public class AncientWorldGenerator {
	private static final FeatureRarity RARITY_LOOT_POD = new FeatureRarity(4, 4, 10);
	private static final FeatureRarity RARITY_SKY_BASE = new FeatureRarity(16, 16, 1);
	
	private static final WorldGenLootPod worldGenLootPod = new WorldGenLootPod();
	private static final WorldGenSkyBase worldGenSkyBase = new WorldGenSkyBase();
	
	private static class FeatureRarity {
		private final int padX, padZ, odds;
		
		public FeatureRarity(int x, int z, int o){
			padX = x;
			padZ = z;
			odds = o;
		}
		
		public boolean match(int x, int z, Random r){
			return x % padX == 0 && z % padZ == 0 && r.nextInt(odds) == 0;
		}
	}
	
	public static void generateLootPod(Random r, int cX, int cZ, World w, IChunkProvider chunkGen, IChunkProvider provider){
		if(RARITY_LOOT_POD.match(cX, cZ, r)){
			worldGenLootPod.generate(r, cX, cZ, w, chunkGen, provider);
		}
	}
	
	public static void generateSkyBase(Random r, int cX, int cZ, World w, IChunkProvider chunkGen, IChunkProvider provider){
		if(RARITY_SKY_BASE.match(cX, cZ, r)){
			worldGenSkyBase.generate(r, cX, cZ, w, chunkGen, provider);
		}
	}
}