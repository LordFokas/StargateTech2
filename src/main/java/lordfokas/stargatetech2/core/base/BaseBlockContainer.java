package lordfokas.stargatetech2.core.base;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.world.World;

public abstract class BaseBlockContainer extends BaseBlock implements ITileEntityProvider{
	
	protected BaseBlockContainer(String name){
		super(name);
	}
	
	protected BaseBlockContainer(String name, boolean breakable, boolean requiresTool) {
		super(name, breakable, requiresTool);
	}
	
	@Override
	public final BaseTileEntity createTileEntity(World world, int metadata){
		return createTileEntity(metadata);
	}
	
	@Override
	public final BaseTileEntity createNewTileEntity(World world, int metadata){
		return createTileEntity(metadata);
	}
	
	protected abstract BaseTileEntity createTileEntity(int metadata);
}