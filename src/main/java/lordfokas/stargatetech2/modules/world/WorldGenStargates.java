package lordfokas.stargatetech2.modules.world;

import java.util.Random;

import lordfokas.stargatetech2.modules.transport.stargates.StargateNetwork;
import lordfokas.stargatetech2.modules.world.genlists.StargateBaseBuildList;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;

public class WorldGenStargates {
	private StargateBaseBuildList builder = new StargateBaseBuildList();
	
	public void generate(Random r, int cX, int cZ, World w, IChunkProvider chunkGen, IChunkProvider provider){
		int x = cX * 16 + 7;
		int z = cZ * 16 + 7;
		int y = 60;
		while(!w.canBlockSeeTheSky(x, y, z)){
			y++;
		}
		for(int i = -3; i <= 3; i++){
			for(int j = -3; j <= 3; j++){
				if(w.getBiomeGenForCoords(x, z).biomeID != BiomeGenBase.desert.biomeID || !w.canBlockSeeTheSky(x, y+2, z)){
					return;
				}
				
			}
		}
		if(StargateNetwork.instance().canPlaceStargateAt(w, x+3, y+5, z)){
			if(w.getBlock(x, y-1, z) == Blocks.sand){
				builder.build(w, x, y, z, StargateBaseBuildList.MATERIAL);
			}
		}
	}
}
