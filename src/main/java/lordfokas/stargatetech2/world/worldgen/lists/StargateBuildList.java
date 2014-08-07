package lordfokas.stargatetech2.world.worldgen.lists;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import lordfokas.stargatetech2.core.util.Vec3Int;
import lordfokas.stargatetech2.transport.ModuleTransport;
import lordfokas.stargatetech2.transport.block.BlockStargate;
import lordfokas.stargatetech2.transport.tileentity.TileStargateRing;

public class StargateBuildList extends BuildList {
	private static final BuildMaterial[] MATERIAL = new BuildMaterial[]{
		new BuildMaterial(ModuleTransport.stargate.blockID, BlockStargate.META_BASE),
		new BuildMaterial(ModuleTransport.stargate.blockID, BlockStargate.META_RING)
	};
	
	public static StargateBuildList SGX = new StargateBuildList(1, 0);
	public static StargateBuildList SGZ = new StargateBuildList(0, 1);
	
	private StargateBuildList(int x, int z){
		for(int i = 1; i <= 2; i++){
			set( x * i, 0,  z * i, 0);
			set(-x * i, 0, -z * i, 0);
		}
		for(int i = 1; i < 5; i++){
			set( 2 * x, i,  2 * z, 1);
			set(-2 * x, i, -2 * z, 1);
		}
		set( x, 4,  z, 1);
		set(-x, 4, -z, 1);
		set( 0, 4,  0, 1);
	}
	
	public void buildStargate(World w, int x, int y, int z){
		build(w, x, y, z, MATERIAL, new Vec3Int(x, y, z));
	}
	
	@Override
	protected void afterBlock(World w, int x, int y, int z, Object o){
		Vec3Int sg = (Vec3Int) o;
		TileEntity te = w.getBlockTileEntity(x, y, z);
		if(te instanceof TileStargateRing){
			((TileStargateRing)te).setSGPosition(sg.x, sg.y, sg.z);
		}
	}
	
	public void delete(World w, int x, int y, int z){
		for(BuildBlock block : blocks){
			w.setBlockToAir(block.x + x, block.y + y, block.z + z);
		}
	}
}
