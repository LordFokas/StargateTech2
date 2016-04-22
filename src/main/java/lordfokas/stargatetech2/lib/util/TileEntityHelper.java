package lordfokas.stargatetech2.lib.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityHelper {
	public static <TYPE> TYPE getTileEntityAs(World w, int x, int y, int z, Class<TYPE> type){
		TileEntity te = w.getTileEntity(x, y, z);
		if(te != null && type.isAssignableFrom(te.getClass())){
			return (TYPE) te;
		}else return null;
	}
}
