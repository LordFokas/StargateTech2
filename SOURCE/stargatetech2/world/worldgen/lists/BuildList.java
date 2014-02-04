package stargatetech2.world.worldgen.lists;

import java.util.ArrayList;

import net.minecraft.world.World;
import stargatetech2.core.util.Vec3Int;
import stargatetech2.core.util.WeakBlockRegistry;

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
	
	protected final ArrayList<BuildBlock> blocks = new ArrayList<BuildBlock>();
	
	public final boolean checkHasSpace(World w, int x, int y, int z){
		for(BuildBlock block : blocks){
			int bx = block.x + x;
			int by = block.y + y;
			int bz = block.z + z;
			int id = w.getBlockId(bx, by, bz);
			int meta = w.getBlockMetadata(bx, by, bz);
			if(!(w.isAirBlock(bx, by, bz) || WeakBlockRegistry.isRemovable(id, meta))){
				return false;
			}
		}
		return true;
	}
	
	public final void build(World w, int x, int y, int z, BuildMaterial[] m){
		build(w, x, y, z, m, null);
	}
	
	public final void build(World w, int x, int y, int z, BuildMaterial[] materials, Object obj){
		for(BuildBlock block : blocks){
			BuildMaterial material = materials[block.id];
			w.setBlock(block.x + x, block.y + y, block.z + z, material.id, material.meta, 3);
			afterBlock(w, block.x + x, block.y + y, block.z + z, obj);
		}
		afterBuild(w, x, y, z, obj);
	}
	
	protected void afterBuild(World w, int x, int y, int z, Object o){}
	protected void afterBlock(World w, int x, int y, int z, Object o){}
	
	protected final void set(int x, int y, int z, int i){
		blocks.add(new BuildBlock(x, y, z, i));
	}
}