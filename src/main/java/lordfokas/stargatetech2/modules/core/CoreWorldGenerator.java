package lordfokas.stargatetech2.modules.core;

import java.util.Random;

import lordfokas.stargatetech2.api.world.EventWorldGen;
import lordfokas.stargatetech2.api.world.EventWorldGen.GenType;
import lordfokas.stargatetech2.modules.ModuleCore;
import lordfokas.stargatetech2.util.ConfigServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

public class CoreWorldGenerator implements IWorldGenerator {

	@Override
	public void generate(Random random, int cX, int cZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider){
		if(world.provider.getDimensionId() == 1 || world.provider.getDimensionId() == -1 || ConfigServer.wgNaquadah < 1) return;
		if((random.nextInt() % ConfigServer.wgNaquadah) != 0) return;
			
		EventWorldGen event = new EventWorldGen(world, cX, cZ, GenType.VEIN_NAQUADAH);
		MinecraftForge.ORE_GEN_BUS.post(event);
		if (event.getResult() == Result.DENY) return;
		
		WorldGenMinable wgm = new WorldGenMinable(ModuleCore.naquadahBlock, 20);
		wgm.generate(world, random, new BlockPos((cX * 16) + (random.nextInt() % 16), 16 + (random.nextInt() % 20), (cZ * 16) + (random.nextInt() % 16)));
	}
}