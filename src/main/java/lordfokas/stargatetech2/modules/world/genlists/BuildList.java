package lordfokas.stargatetech2.modules.world.genlists;

import java.util.ArrayList;

import lordfokas.stargatetech2.util.Vec3Int;
import lordfokas.stargatetech2.util.api.WeakBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public abstract class BuildList {
	protected static class BuildBlock extends Vec3Int{
		public final int id;
		
		public BuildBlock(int x, int y, int z, int id) {
			super(x, y, z);
			this.id = id;
		}
	}
	
	public static class BuildMaterial{
		public final Block bl;
		public final int meta;
		
		public BuildMaterial(Block b, int m){
			this.bl = b;
			this.meta = m;
		}
		
		public BuildMaterial(Block b){
			this(b, 0);
		}
	}
	
	protected final ArrayList<BuildBlock> blocks = new ArrayList<BuildBlock>();
	
	public final boolean checkHasSpace(World w, int x, int y, int z){
		for(BuildBlock block : blocks){
			int bx = block.x + x;
			int by = block.y + y;
			int bz = block.z + z;
			Block b = w.getBlock(bx, by, bz);
			int meta = w.getBlockMetadata(bx, by, bz);
			if(!(w.isAirBlock(bx, by, bz) || WeakBlockRegistry.isRemovable(b, meta))){
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
			w.setBlock(block.x + x, block.y + y, block.z + z, material.bl, material.meta, 3);
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