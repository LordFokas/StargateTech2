package lordfokas.stargatetech2.modules.core.base__THRASH;

import lordfokas.stargatetech2.lib.block.BaseBlock;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.world.World;

public abstract class BaseBlockContainer__OLD_AND_FLAWED extends BaseBlock implements ITileEntityProvider{
	
	public BaseBlockContainer__OLD_AND_FLAWED(String name){
		super(name);
	}
	
	public BaseBlockContainer__OLD_AND_FLAWED(String name, boolean breakable, boolean requiresTool) {
		super(name, breakable, requiresTool);
	}
	
	@Override
	public final BaseTileEntity__OLD_AND_FLAWED createTileEntity(World world, int metadata){
		return createTileEntity(metadata);
	}
	
	@Override
	public final BaseTileEntity__OLD_AND_FLAWED createNewTileEntity(World world, int metadata){
		return createTileEntity(metadata);
	}
	
	protected abstract BaseTileEntity__OLD_AND_FLAWED createTileEntity(int metadata);
}