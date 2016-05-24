package lordfokas.stargatetech2.modules.automation;

import lordfokas.naquadria.block.BaseBlock;
import lordfokas.stargatetech2.api.bus.IBusDevice;
import lordfokas.stargatetech2.api.bus.IBusInterface;
import lordfokas.stargatetech2.reference.BlockReference;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockBusCable extends BaseBlock {
	
	public BlockBusCable() {
		super(BlockReference.BUS_CABLE, true, false);
		// setRenderer(RenderBusCable.instance());
		setIsAbstractBusBlock();
		setLightOpacity(0);
	}
	
	@Override
	public boolean isOpaqueCube(){
		return false;
	}
	
	/*@Override
	public boolean renderAsNormalBlock(){
		return false;
	}*/
	
	public Connection getBusConnection(World world, BlockPos pos, EnumFacing d){
		pos = pos.offset(d);
		if(world.getBlockState(pos).getBlock() == this) return Connection.CABLE;
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof IBusDevice){
			IBusInterface[] interfaces = ((IBusDevice)te).getInterfaces(d.getOpposite().ordinal());
			if(interfaces == null || interfaces.length == 0) return Connection.DISCONNECTED;
			for(IBusInterface networkCard : interfaces){
				if(networkCard instanceof BusInterface){
					return Connection.DEVICE;
				}
			}
		}
		return Connection.DISCONNECTED;
	}
	
	/*@Override
	public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer p, int s, float hx, float hy, float hz){
		ItemStack stack = p.inventory.getCurrentItem();
		Item item = stack != null ? stack.getItem() : null;
		if(item instanceof IToolWrench){
			IToolWrench wrench = (IToolWrench) item;
			if(wrench.canWrench(p, x, y, z)){
				dropBlockAsItem(w, x, y, z, 0, 0);
				w.setBlock(x, y, z, Blocks.air, 0, 3);
				wrench.wrenchUsed(p, x, y, z);
				return true;
			}
		}
		return false;
	}*/
	
	@Override // FIXME why is this still a thing?
	public void setBlockBoundsForItemRender(){
		this.setBlockBounds(0.3125F, 0F, 0.3125F, 0.6875F, 1F, 0.6875F);
	}
	
	/*@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z){
		float x0 = getBusConnection(world, x, y, z, ForgeDirection.WEST ).isConnected() ? 0 : 0.3125F;
		float x1 = getBusConnection(world, x, y, z, ForgeDirection.EAST ).isConnected() ? 1 : 0.6875F;
		float y0 = getBusConnection(world, x, y, z, ForgeDirection.DOWN ).isConnected() ? 0 : 0.3125F;
		float y1 = getBusConnection(world, x, y, z, ForgeDirection.UP   ).isConnected() ? 1 : 0.6875F;
		float z0 = getBusConnection(world, x, y, z, ForgeDirection.NORTH).isConnected() ? 0 : 0.3125F;
		float z1 = getBusConnection(world, x, y, z, ForgeDirection.SOUTH).isConnected() ? 1 : 0.6875F;
		setBlockBounds(x0, y0, z0, x1, y1, z1);
	}*/
	
	/*@Override
	public void addCollisionBoxesToList(World w, int x, int y, int z, AxisAlignedBB aabb, List list, Entity entity){
		this.setBlockBoundsBasedOnState(w, x, y, z);
		super.addCollisionBoxesToList(w, x, y, z, aabb, list, entity);
	}*/
}