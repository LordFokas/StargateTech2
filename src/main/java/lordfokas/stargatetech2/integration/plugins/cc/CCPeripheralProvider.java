package lordfokas.stargatetech2.integration.plugins.cc;

import net.minecraft.world.World;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;

public class CCPeripheralProvider implements IPeripheralProvider{

	@Override
	public IPeripheral getPeripheral(World world, int x, int y, int z, int side) {
		if(world.getBlockTileEntity(x, y, z)!=null && world.getBlockTileEntity(x, y, z) instanceof TileBusAdapter)
			if(((TileBusAdapter)world.getBlockTileEntity(x, y, z)).canAttachToSide(side))
				return (IPeripheral) world.getBlockTileEntity(x, y, z);
			else
				return null;
		else
			return null;
	}

}
