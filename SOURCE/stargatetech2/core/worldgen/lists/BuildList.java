package stargatetech2.core.worldgen.lists;

import java.util.ArrayList;

import net.minecraft.world.World;

import stargatetech2.common.util.Vec3Int;

public abstract class BuildList {
	protected static class BuildBlock extends Vec3Int{
		public final int id;
		
		public BuildBlock(int x, int y, int z, int id) {
			super(x, y, z);
			this.id = id;
		}
	}
	
	public static class BuildMaterial{
		public final int id, meta;
		
		public BuildMaterial(int i, int m){
			this.id = i;
			this.meta = m;
		}
		
		public BuildMaterial(int i){
			this(i, 0);
		}
	}
	
	private final ArrayList<BuildBlock> blocks = new ArrayList<BuildBlock>();
	
	public final void build(World w, int x, int y, int z, BuildMaterial[] materials){
		for(BuildBlock block : blocks){
			BuildMaterial material = materials[block.id];
			w.setBlock(block.x + x, block.y + y, block.z + z, material.id, material.meta, 3);
		}
		afterBuild(w, x, y, z);
	}
	
	protected abstract void afterBuild(World w, int x, int y, int z);
	
	protected final void set(int x, int y, int z, int i){
		blocks.add(new BuildBlock(x, y, z, i));
	}
}