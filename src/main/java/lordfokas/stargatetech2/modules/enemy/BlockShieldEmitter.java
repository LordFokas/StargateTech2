package lordfokas.stargatetech2.modules.enemy;

import lordfokas.stargatetech2.ZZ_THRASH.BlockMachine__THRASH;
import lordfokas.stargatetech2.ZZ_THRASH.Vec3Int_THRASH;
import lordfokas.stargatetech2.reference.BlockReference;
import lordfokas.stargatetech2.reference.TextureReference;
import lordfokas.stargatetech2.util.IconRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockShieldEmitter extends BlockMachine__THRASH{

	public BlockShieldEmitter() {
		super(BlockReference.SHIELD_EMITTER, true);
		super.setUseVertical();
	}
	
	@Override // TODO: SO MUCH SHIT TO CLEAN DUDE!
	public boolean canPlaceBlockAt(World w, int x, int y, int z){
		if(!super.canPlaceBlockAt(w, x, y, z)) return false;
		Vec3Int_THRASH controller = null;
		for(ForgeDirection fd : ForgeDirection.VALID_DIRECTIONS){
			int sx = x + fd.offsetX;
			int sy = y + fd.offsetY;
			int sz = z + fd.offsetZ;
			if(sy >= 0 && sy < w.getActualHeight()){ // make sure we're within vertical world bounds;
				TileEntity te = w.getTileEntity(sx, sy, sz);
				Vec3Int_THRASH c = null;
				boolean wasNull = false;
				if(te instanceof IShieldControllerProvider){
					c = ((IShieldControllerProvider)te).getShieldControllerCoords();
					if(controller == null){
						controller = c; // make sure we always have a controller;
						wasNull = true;
					}
				}else if(te instanceof TileShieldController){
					c = new Vec3Int_THRASH(sx, sy, sz);
					if(controller == null){
						controller = c; // make sure we always have a controller;
						wasNull = true;
					}
				}
				if(c != null && !wasNull && !controller.equals(c)) return false; // make sure there's no 2 different controllers;
			}
		}
		return controller != null; // a single controller was found;
	}
	
	@Override
	protected void onPlacedBy(World w, int x, int y, int z, EntityPlayer player, ForgeDirection facing){
		TileEntity te = w.getTileEntity(x, y, z);
		if(te instanceof TileShieldEmitter){ // the power of copy-pasta. TODO: clean this mess.
			for(ForgeDirection fd : ForgeDirection.VALID_DIRECTIONS){
				int sx = x + fd.offsetX;
				int sy = y + fd.offsetY;
				int sz = z + fd.offsetZ;
				if(sy >= 0 && sy < w.getActualHeight()){ // make sure we're within vertical world bounds;
					TileEntity prvdr = w.getTileEntity(sx, sy, sz);
					if(prvdr instanceof IShieldControllerProvider){
						Vec3Int_THRASH controller = ((IShieldControllerProvider)prvdr).getShieldControllerCoords();
						if(controller != null){
							((TileShieldEmitter)te).setController(controller);
							return;
						}
					}else if(prvdr instanceof TileShieldController){
						((TileShieldEmitter)te).setController(new Vec3Int_THRASH(prvdr.xCoord, prvdr.yCoord, prvdr.zCoord));
						return;
					}
				}
			}
			throw new RuntimeException("Someone f***ed up, and it ain't me!");
		}
	}
	
	@Override
	public IIcon getFaceForMeta(int meta){
		if(meta == 0) return IconRegistry.blockIcons.get(TextureReference.SHIELD_EMITTER_B);
		if(meta == 1) return IconRegistry.blockIcons.get(TextureReference.SHIELD_EMITTER_T);
		return super.getFaceForMeta(meta);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileShieldEmitter();
	}
}