package stargatetech2.enemy.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import stargatetech2.core.machine.BlockMachine;
import stargatetech2.core.machine.TileMachine;
import stargatetech2.core.reference.BlockReference;
import stargatetech2.core.util.Vec3Int;
import stargatetech2.enemy.util.IShieldControllerProvider;

public class BlockShieldEmitter extends BlockMachine{

	public BlockShieldEmitter() {
		super(BlockReference.SHIELD_EMITTER, true);
	}

	@Override
	protected TileMachine createTileEntity(int metadata) {
		return null;
	}
	
	public boolean canPlaceBlockAt(World w, int x, int y, int z){
		if(!super.canPlaceBlockAt(w, x, y, z)) return false;
		Vec3Int controller = null;
		for(ForgeDirection fd : ForgeDirection.VALID_DIRECTIONS){
			int sx = x + fd.offsetX;
			int sy = y + fd.offsetY;
			int sz = z + fd.offsetZ;
			if(sy >= 0 && sy < w.getActualHeight()){ // make sure we're within vertical world bounds;
				TileEntity te = w.getBlockTileEntity(sx, sy, sz);
				if(te instanceof IShieldControllerProvider){
					Vec3Int c = ((IShieldControllerProvider)te).getShieldControllerCoords();
					if(controller == null) controller = c; // make sure we always have a controller;
					if(!controller.equals(c)) return false; // make sure there's no 2 different controllers;
				}
			}
		}
		return controller != null; // a single controller was found;
	}
}