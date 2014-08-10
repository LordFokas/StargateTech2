package lordfokas.stargatetech2.world.worldgen;

import java.util.ArrayList;
import java.util.Random;

import lordfokas.stargatetech2.world.worldgen.lists.BuildList.BuildMaterial;
import lordfokas.stargatetech2.world.worldgen.lists.LootPodBuildList;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;

public class WorldGenLootPod {
	private static final ArrayList<Block> VALID_GROUND = new ArrayList<Block>();
	private static final ArrayList<BuildMaterial[]> MATERIALS = new ArrayList<BuildMaterial[]>();
	private static final LootPodBuildList BUILD_LIST = new LootPodBuildList();
	
	static{
		addMaterials(Blocks.sand, BUILD_LIST.sands);
		addMaterials(Blocks.grass, BUILD_LIST.stone);
	}
	
	private static void addMaterials(Block b, BuildMaterial[] list){
		VALID_GROUND.add(b);
		MATERIALS.add(list);
	}
	
	public void generate(Random r, int cX, int cZ, World w, IChunkProvider chunkGen, IChunkProvider provider){
		int x = cX * 16 + r.nextInt(16);
		int y = 60;
		int z = cZ * 16 + r.nextInt(16);
		BuildMaterial[] list = null;
		
		while(!w.canBlockSeeTheSky(x, y, z)){
			y++;
		}
		y--;
		
		Block b = w.getBlock(x, y, z);
		if(VALID_GROUND.contains(b) && w.getBiomeGenForCoords(x, z).biomeID != BiomeGenBase.desert.biomeID){
			int index = VALID_GROUND.indexOf(b);
			list = MATERIALS.get(index);
		}
		if(list != null){
			BUILD_LIST.build(w, x, y, z, list);
		}
	}
}