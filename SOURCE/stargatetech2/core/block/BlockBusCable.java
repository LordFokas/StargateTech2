package stargatetech2.core.block;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import stargatetech2.api.bus.IBusDevice;
import stargatetech2.api.bus.IBusInterface;
import stargatetech2.common.base.BaseBlock;
import stargatetech2.common.reference.BlockReference;
import stargatetech2.core.network.bus.BusInterface;
import stargatetech2.core.network.bus.Connection;
import stargatetech2.core.rendering.RenderBusCable;

public class BlockBusCable extends BaseBlock {
	
	public BlockBusCable() {
		super(BlockReference.BUS_CABLE, true, false);
		setIsAbstractBusBlock();
		setLightOpacity(0);
	}
	
	@Override
	public int getRenderType(){
		return RenderBusCable.instance().getRenderId();
	}
	
	@Override
	public boolean isOpaqueCube(){
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock(){
		return false;
	}
	
	public Connection getBusConnection(IBlockAccess world, int x, int y, int z, ForgeDirection d){
		if(world.getBlockId(x + d.offsetX, y + d.offsetY, z + d.offsetZ) == blockID) return Connection.CABLE;
		TileEntity te = world.getBlockTileEntity(x + d.offsetX, y + d.offsetY, z + d.offsetZ);
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
	
	@Override
	public void setBlockBoundsForItemRender(){
		this.setBlockBounds(0.3125F, 0F, 0.3125F, 0.6875F, 1F, 0.6875F);
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z){
		float x0 = getBusConnection(world, x, y, z, ForgeDirection.WEST ).isConnected() ? 0 : 0.3125F;
		float x1 = getBusConnection(world, x, y, z, ForgeDirection.EAST ).isConnected() ? 1 : 0.6875F;
		float y0 = getBusConnection(world, x, y, z, ForgeDirection.DOWN ).isConnected() ? 0 : 0.3125F;
		float y1 = getBusConnection(world, x, y, z, ForgeDirection.UP   ).isConnected() ? 1 : 0.6875F;
		float z0 = getBusConnection(world, x, y, z, ForgeDirection.NORTH).isConnected() ? 0 : 0.3125F;
		float z1 = getBusConnection(world, x, y, z, ForgeDirection.SOUTH).isConnected() ? 1 : 0.6875F;
		setBlockBounds(x0, y0, z0, x1, y1, z1);
	}
	
	@Override
	public void addCollisionBoxesToList(World w, int x, int y, int z, AxisAlignedBB aabb, List list, Entity entity){
		this.setBlockBoundsBasedOnState(w, x, y, z);
		super.addCollisionBoxesToList(w, x, y, z, aabb, list, entity);
	}
}