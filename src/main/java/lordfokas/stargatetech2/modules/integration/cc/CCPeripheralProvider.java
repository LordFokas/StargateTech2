package lordfokas.stargatetech2.modules.integration.cc;

import net.minecraft.world.World;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;

public class CCPeripheralProvider implements IPeripheralProvider{

	@Override
	public IPeripheral getPeripheral(World world, int x, int y, int z, int side) {
		if(world.getTileEntity(x, y, z)!=null && world.getTileEntity(x, y, z) instanceof TileBusAdapter)
			if(((TileBusAdapter)world.getTileEntity(x, y, z)).canAttachToSide(side))
				return (IPeripheral) world.getTileEntity(x, y, z);
			else
				return null;
		else
			return null;
	}

}
