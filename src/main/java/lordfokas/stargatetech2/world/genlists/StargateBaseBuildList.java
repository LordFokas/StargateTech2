package lordfokas.stargatetech2.world.genlists;

import lordfokas.stargatetech2.core.api.SeedingShip;
import lordfokas.stargatetech2.world.LootGenerator;
import lordfokas.stargatetech2.world.LootGenerator.LootLevel;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class StargateBaseBuildList extends BuildList {
	public static final BuildMaterial[] MATERIAL = new BuildMaterial[]{
		new BuildMaterial(Blocks.sandstone, 0),
		new BuildMaterial(Blocks.sandstone, 2),
		new BuildMaterial(Blocks.sandstone, 1),
		new BuildMaterial(Blocks.torch, 0),
		new BuildMaterial(Blocks.sandstone_stairs, 3),
		new BuildMaterial(Blocks.sandstone_stairs, 2),
		new BuildMaterial(Blocks.sandstone_stairs, 0),
		new BuildMaterial(Blocks.sandstone_stairs, 1),
		new BuildMaterial(Blocks.air, 0)
	};
	
	public StargateBaseBuildList(){
		// ### MAIN BLOCK ### ### ###
		for(int x = -3; x <= 3; x++){
			for(int z = -3; z <= 3; z++){
				for(int y = -5; y < 0; y++){
					set(x, y, z, 0);
				}
				set(x, 0, z, 1);
				set(x, 1, z, 8);
				set(x, 2, z, 8);
				set(x, 3, z, 8);
				set(x, 4, z, 1);
			}
		}
		for(int y = 0; y <= 5; y++){
			set(-3, y, -3, 2);
			set(-3, y,  3, 2);
			set( 3, y, -3, 2);
			set( 3, y,  3, 2);
		}
		set(-2, 1, -2, 3);
		set(-2, 1,  2, 3);
		set( 2, 1, -2, 3);
		set( 2, 1,  2, 3);
		set(-3, 6, -3, 3);
		set(-3, 6,  3, 3);
		set( 3, 6, -3, 3);
		set( 3, 6,  3, 3);
		for(int y = 0; y < 4; y++){
			for(int i = -1; i <= 1; i++){
				set(i, y, -4, 1);
				set(i, y,  4, 1);
				set(-4, y, i, 1);
				set( 4, y, i, 1);
			}
			set(-2, y, -4, 2);
			set( 2, y, -4, 2);
			set(-2, y,  4, 2);
			set( 2, y,  4, 2);
			set(-4, y, -2, 2);
			set( 4, y, -2, 2);
			set(-4, y,  2, 2);
			set( 4, y,  2, 2);
		}
		for(int i = -2; i <= 2; i++){
			set(i, 4,  4, 4);
			set(i, 4, -4, 5);
			set(-4, 4, i, 6);
			set( 2, 5, i, 6);
			set( 4, 5, i, 7);
			set( 4, 4, i, 1);
		}
		set( 2, 5,  3, 4);
		set( 2, 5, -3, 5);
		
		// ### STAIRS ### ### ###
		for(int j = 3; j >= 0; j--){
			int k = 8 - j;
			for(int i = -2; i <= 2; i++){
				for(int y = 0; y < j; y++){
					set(i, y,  k, 2);
					set(i, y, -k, 2);
					set(-k, y, i, 2);
				}
				set(i, j,  k, 4);
				set(i, j, -k, 5);
				set(-k, j, i, 6);
			}
		}
	}
	
	@Override
	protected void afterBuild(World w, int x, int y, int z, Object o){
		SeedingShip.SHIP.placeStargate(w, x+3, y+5, z, 3);
		LootGenerator.generateLootChest(w, x, y + 1, z - 3, LootLevel.RARE);
		w.setBlockMetadataWithNotify(x, y + 1, z - 3, 3, 3);
		LootGenerator.generateLootChest(w, x, y + 1, z + 3, LootLevel.RARE);
		w.setBlockMetadataWithNotify(x, y + 1, z + 3, 2, 3);
		LootGenerator.generateLootChest(w, x - 3, y + 1, z, LootLevel.EPIC);
		w.setBlockMetadataWithNotify(x - 3, y + 1, z, 5, 3);
		destroyStairs(w, x, y, z);
	}
	
	private void destroyStairs(World w, int x, int y, int z){
		for(int i = -2; i <= 2; i++){
			for(int j = 5; j <= 8; j++){
				if(!w.isSideSolid(x + i, y-1, z + j, ForgeDirection.UP, false)) destroyColumn(w, x + i, y, z + j);
				if(!w.isSideSolid(x + i, y-1, z - j, ForgeDirection.UP, false)) destroyColumn(w, x + i, y, z - j);
				if(!w.isSideSolid(x - j, y-1, z + i, ForgeDirection.UP, false)) destroyColumn(w, x - j, y, z + i);
			}
		}
	}
	
	private void destroyColumn(World w, int x, int y, int z){
		for(int i = 0; i < 4; i++){
			w.setBlockToAir(x, y + i, z);
		}
	}
}