package lordfokas.stargatetech2.modules.world.genlists;

import java.util.Random;

import lordfokas.stargatetech2.lib.render.Color;
import lordfokas.stargatetech2.modules.ModuleWorld;
import lordfokas.stargatetech2.modules.world.LootGenerator;
import lordfokas.stargatetech2.modules.world.LootGenerator.LootLevel;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;


public class LootPodBuildList extends BuildList {
	public final BuildMaterial[] stone;
	public final BuildMaterial[] sands;
	
	public LootPodBuildList(){
		stone = new BuildMaterial[]{
					new BuildMaterial(Blocks.stonebrick, 0),
					new BuildMaterial(Blocks.stonebrick, 3),
					new BuildMaterial(Blocks.stonebrick, 3),
					new BuildMaterial(ModuleWorld.lanteanWall, Color.LIGHT_GRAY.id),
					new BuildMaterial(Blocks.stone_slab)
				};
		
		sands = new BuildMaterial[]{
					new BuildMaterial(Blocks.sandstone, 0),
					new BuildMaterial(Blocks.sandstone, 2),
					new BuildMaterial(Blocks.sandstone, 1),
					new BuildMaterial(ModuleWorld.lanteanWall, Color.ORANGE.id),
					new BuildMaterial(Blocks.sandstone, 0)
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