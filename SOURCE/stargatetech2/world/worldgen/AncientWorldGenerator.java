package stargatetech2.world.worldgen;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import stargatetech2.core.util.ConfigServer;

public class AncientWorldGenerator {
	private static final FeatureRarity RARITY_LOOT_POD = new FeatureRarity(ConfigServer.wgLootPodGap, ConfigServer.wgLootPodGap, ConfigServer.wgLootPodOdd);
	private static final FeatureRarity RARITY_STARGATE = new FeatureRarity(8, 8, 4);
	
	private static final WorldGenLootPod worldGenLootPod = new WorldGenLootPod();
	private static final WorldGenStargates worldGenStargate = new WorldGenStargates();
	
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
	
	public static void generateStargate(Random r, int cX, int cZ, World w, IChunkProvider chunkGen, IChunkProvider provider){
		if(RARITY_STARGATE.match(cX, cZ, r)){
			worldGenStargate.generate(r, cX, cZ, w, chunkGen, provider);
		}
	}
}