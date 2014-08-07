package lordfokas.stargatetech2.world.worldgen.lists;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import lordfokas.stargatetech2.core.util.Color;
import lordfokas.stargatetech2.world.ModuleWorld;
import lordfokas.stargatetech2.world.worldgen.LootGenerator;
import lordfokas.stargatetech2.world.worldgen.LootGenerator.LootLevel;


public class LootPodBuildList extends BuildList {
	public final BuildMaterial[] stone;
	public final BuildMaterial[] sands;
	
	public LootPodBuildList(){
		stone = new BuildMaterial[]{
					new BuildMaterial(Block.stoneBrick.blockID, 0),
					new BuildMaterial(Block.stoneBrick.blockID, 3),
					new BuildMaterial(Block.stoneBrick.blockID, 3),
					new BuildMaterial(ModuleWorld.lanteanWall.blockID, Color.LIGHT_GRAY.id),
					new BuildMaterial(43)
				};
		
		sands = new BuildMaterial[]{
					new BuildMaterial(Block.sandStone.blockID, 0),
					new BuildMaterial(Block.sandStone.blockID, 2),
					new BuildMaterial(Block.sandStone.blockID, 1),
					new BuildMaterial(ModuleWorld.lanteanWall.blockID, Color.ORANGE.id),
					new BuildMaterial(Block.sandStone.blockID, 0)
				};
		
		for(int i = -1; i < 2; i++){
			for(int j = -1; j < 2; j++){
				set(i, 5, j, 0);
				set(i, 0, j, 4);
				set(i, -1, j, 0);
			}
		}
		set(0, -2, 0, 0);
		for(int i = -2; i < 3; i++){
			for(int j = -2; j < 3; j++){
				if(i == -2 || i == 2 || j == -2 || j == 2){
					set(i, 0, j, 1);
					set(i, 5, j, 1);
				}
			}
		}
		for(int i = 1; i < 5; i++){
			int m = i == 2 ? 3 : 2;
			set(-2, i, -2, m);
			set( 2, i, -2, m);
			set(-2, i,  2, m);
			set( 2, i,  2, m);
		}
	}

	@Override
	protected void afterBuild(World w, int x, int y, int z, Object o) {
		Random random = new Random();
		LootLevel level;
		int rnd = random.nextInt(100);
		if(rnd < 5){
			level = LootLevel.EPIC;
		}else if(rnd < 25){
			level = LootLevel.RARE;
		}else{
			level = LootLevel.COMMON;
		}
		LootGenerator.generateLootChest(w, x, y, z, level);
	}
}