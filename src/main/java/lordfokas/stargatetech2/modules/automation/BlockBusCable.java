package lordfokas.stargatetech2.modules.automation;

import lordfokas.naquadria.block.BaseBlock;
import lordfokas.naquadria.render.IVariantProvider;
import lordfokas.stargatetech2.api.bus.IBusDevice;
import lordfokas.stargatetech2.api.bus.IBusInterface;
import lordfokas.stargatetech2.reference.BlockReference;
import lordfokas.stargatetech2.util.StargateTab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class BlockBusCable extends BaseBlock implements IVariantProvider {
	
	public BlockBusCable() {
		super(BlockReference.BUS_CABLE, true, false);
		setIsAbstractBusBlock();
		setLightOpacity(0);
	}
	
	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, net.minecraft.util.math.BlockPos pos) {
		return false;
	}
	
	// FIXME: make IBusDevice a Capability.
	public ConnectionType getBusConnection(World world, BlockPos pos, EnumFacing d){
		pos = pos.offset(d);
		if(world.getBlockState(pos).getBlock() == this) return ConnectionType.CABLE;
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof IBusDevice){
			IBusInterface[] interfaces = ((IBusDevice)te).getInterfaces(d.getOpposite());
			if(interfaces == null || interfaces.length == 0) return ConnectionType.DISCONNECTED;
			for(IBusInterface networkCard : interfaces){
				if(networkCard instanceof BusInterface){
					return ConnectionType.DEVICE;
				}
			}
		}
		return ConnectionType.DISCONNECTED;
	}

    @Override
    public void addVariants(List<Pair<Integer, String>> variants) {
        variants.add(Pair.of(0, "normal"));
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
	
	// FIXME why is this still a thing?
	/*@Override 
	public void setBlockBoundsForItemRender(){
		this.setBlockBounds(0.3125F, 0F, 0.3125F, 0.6875F, 1F, 0.6875F);
	}*/
	
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