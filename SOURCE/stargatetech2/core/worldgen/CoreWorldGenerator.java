package stargatetech2.core.worldgen;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import stargatetech2.core.ModuleCore;
import cpw.mods.fml.common.IWorldGenerator;

public class CoreWorldGenerator implements IWorldGenerator {

	@Override
	public void generate(Random random, int cX, int cZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider){
		generateOres(world, random, cX, cZ);
		AncientWorldGenerator.generateLootPod(random, cX, cZ, world, chunkGenerator, chunkProvider);
		AncientWorldGenerator.generateSkyBase(random, cX, cZ, world, chunkGenerator, chunkProvider);
	}
	
	private void generateOres(World world, Random random, int cX, int cZ){
		WorldGenMinable wgm = new WorldGenMinable(ModuleCore.naquadahOre.blockID, 20);
		wgm.generate(world, random, (cX * 16) + (random.nextInt() % 16), 16 + (random.nextInt() % 20), (cZ * 16) + (random.nextInt() % 16));
	}
}