package lordfokas.naquadria.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

public class TileEntityHelper{
	public static <TYPE> TYPE getTileEntityAs(World w, BlockPos pos, Class<TYPE> type){
		TileEntity te = w.getTileEntity(pos);
		if(te != null && type.isAssignableFrom(te.getClass())){
			return (TYPE) te;
		}else return null;
	}
	
	public static <TYPE> TYPE getTileCapability(World w, BlockPos pos, Capability<TYPE> capability, EnumFacing facing){
		TileEntity te = w.getTileEntity(pos);
		if(te == null) return null;
		return te.getCapability(capability, facing);
	}
}
