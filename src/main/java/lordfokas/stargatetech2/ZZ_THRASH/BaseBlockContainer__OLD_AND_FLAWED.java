package lordfokas.stargatetech2.ZZ_THRASH;

import lordfokas.stargatetech2.lib.block.BaseBlock;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

@Deprecated
public abstract class BaseBlockContainer__OLD_AND_FLAWED extends BaseBlock implements ITileEntityProvider{
	
	public BaseBlockContainer__OLD_AND_FLAWED(String name){
		super(name);
	}
	
	public BaseBlockContainer__OLD_AND_FLAWED(String name, boolean breakable, boolean requiresTool) {
		super(name, breakable, requiresTool);
	}
	
	/*@Override
	public final TileEntity createTileEntity(World world, int metadata){
		return createTileEntity(metadata);
	}*/
	
	@Override
	public final TileEntity createNewTileEntity(World world, int metadata){
		return createTileEntity(metadata);
	}
	
	protected abstract TileEntity createTileEntity(int metadata);
}