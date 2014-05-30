package stargatetech2.core.util;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event.Result;
import stargatetech2.api.world.EventWorldGen;
import stargatetech2.api.world.EventWorldGen.GenType;
import stargatetech2.core.ModuleCore;
import cpw.mods.fml.common.IWorldGenerator;

public class CoreWorldGenerator implements IWorldGenerator {

	@Override
	public void generate(Random random, int cX, int cZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider){
		if(world.provider.dimensionId == 1 || world.provider.dimensionId == -1) return;
		
		EventWorldGen event = new EventWorldGen(world, cX, cZ, GenType.VEIN_NAQUADAH);
		MinecraftForge.ORE_GEN_BUS.post(event);
		if (event.getResult() == Result.DENY) return;
		
		WorldGenMinable wgm = new WorldGenMinable(ModuleCore.naquadahOre.blockID, 20);
		wgm.generate(world, random, (cX * 16) + (random.nextInt() % 16), 16 + (random.nextInt() % 20), (cZ * 16) + (random.nextInt() % 16));
	}
}