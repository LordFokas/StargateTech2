package stargatetech2.enemy.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import stargatetech2.api.ITabletAccess;
import stargatetech2.core.MACHINE_BAD.BlockMachineBAD;
import stargatetech2.core.reference.BlockReference;
import stargatetech2.core.reference.TextureReference;
import stargatetech2.core.util.GUIHandler.Screen;
import stargatetech2.enemy.tileentity.TileShieldEmitter;

public class BlockShieldEmitter extends BlockMachineBAD implements ITabletAccess{

	public BlockShieldEmitter() {
		super(BlockReference.SHIELD_EMITTER, Screen.SHIELD_EMITTER);
		setIsAbstractBusBlock();
	}

	@Override
	protected TileShieldEmitter createTileEntity(int metadata) {
		return new TileShieldEmitter();
	}

	@Override
	public String getFace(IBlockAccess world, int x, int y, int z) {
		return TextureReference.FACE_SHIELD_EMITTER + getConnectionSuffix(world, x, y, z);
	}

	@Override
	public String getGlow(IBlockAccess world, int x, int y, int z) {
		return TextureReference.GLOW_SHIELD_EMITTER + getConnectionSuffix(world, x, y, z);
	}
	
	private String getConnectionSuffix(IBlockAccess world, int x, int y, int z){
		int meta = world.getBlockMetadata(x, y, z);
		int connections = 0;
		int m, i;
		m = world.getBlockMetadata(x, y-1, z);
		i = world.getBlockId(x, y-1, z);
		if(m == meta && i == blockID) connections += 2;
		m = world.getBlockMetadata(x, y+1, z);
		i = world.getBlockId(x, y+1, z);
		if(m == meta && i == blockID) connections += 1;
		switch(connections){
			case 1: return "_B";
			case 2: return "_T";
			case 3: return "_D";
			default: return "";
		}
	}
	
	@Override
	protected boolean canAccess(EntityPlayer player, World world, int x, int y, int z){
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if(te instanceof TileShieldEmitter){
			return ((TileShieldEmitter)te).canAccess(player.getDisplayName());
		}
		return false;
	}
}