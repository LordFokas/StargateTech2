package lordfokas.stargatetech2.modules.transport;

import lordfokas.stargatetech2.ZZ_THRASH.BaseTileEntity__OLD_AND_FLAWED;
import lordfokas.stargatetech2.api.bus.IBusDevice;
import net.minecraft.world.World;

public abstract class TileBeacon extends BaseTileEntity__OLD_AND_FLAWED implements IBusDevice{
	
	@Override
	public final boolean canUpdate(){
		return false;
	}
	
	@Override
	public World getWorld() {
		return worldObj;
	}
	
	@Override
	public int getXCoord() {
		return xCoord;
	}
	
	@Override
	public int getYCoord() {
		return yCoord;
	}
	
	@Override
	public int getZCoord() {
		return zCoord;
	}
}