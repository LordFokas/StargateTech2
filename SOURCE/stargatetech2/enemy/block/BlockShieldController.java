package stargatetech2.enemy.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import stargatetech2.core.machine.BlockMachine;
import stargatetech2.core.machine.TileEntityMachine;
import stargatetech2.core.reference.BlockReference;
import stargatetech2.core.util.GUIHandler.Screen;
import stargatetech2.enemy.tileentity.TileShieldController;

public class BlockShieldController extends BlockMachine {

	public BlockShieldController() {
		super(BlockReference.SHIELD_CONTROLLER, true, Screen.SHIELD_CONTROLLER);
	}
	
	@Override
	protected void onPlacedBy(World w, int x, int y, int z, EntityPlayer player, ForgeDirection facing){
		TileEntity te = w.getBlockTileEntity(x, y, z);
		if(te instanceof TileShieldController){
			((TileShieldController)te).setOwner(player);
		}
	}
	
	@Override
	protected boolean canPlayerAccess(EntityPlayer player, World world, int x, int y, int z){
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if(te instanceof TileShieldController){
			return ((TileShieldController)te).isOwner(player);
		}else{
			return true;
		}
	}
	
	@Override
	protected TileEntityMachine createTileEntity(int metadata) {
		return new TileShieldController();
	}
}