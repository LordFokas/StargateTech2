package lordfokas.stargatetech2.lib.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class BaseBlockContainer extends BaseBlock implements ITileEntityProvider{
	public BaseBlockContainer(String uName) {
		super(uName);
	}
	
	public BaseBlockContainer(String uName, boolean breakable, boolean requiresTool) {
		super(uName, breakable, requiresTool);
	}
	
	public BaseBlockContainer(String uName, boolean breakable, Material material) {
		super(uName, breakable, material);
	}
	
	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return this.createNewTileEntity(world, metadata);
	}
	
	@Override // This is just so that the method comes out with proper names.
	public abstract TileEntity createNewTileEntity(World world, int metadata);
}
