package stargatetech2.common.base;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class BaseBlockContainer extends BaseBlock implements ITileEntityProvider{
	
	protected BaseBlockContainer(String name){
		super(name);
	}
	
	protected BaseBlockContainer(String name, boolean breakable, boolean requiresTool) {
		super(name, breakable, requiresTool);
	}

	@Override // This doesn't really get used.
	public BaseTileEntity createNewTileEntity(World world){
		return createTileEntity(0);
	}
	
	@Override
	public final BaseTileEntity createTileEntity(World world, int metadata){
		return createTileEntity(metadata);
	}
	
	// World is never used. At least will not be for a long time.
	// Metadata is all I need :D
	protected abstract BaseTileEntity createTileEntity(int metadata);
}