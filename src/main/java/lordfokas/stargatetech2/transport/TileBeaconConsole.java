package lordfokas.stargatetech2.transport;

import lordfokas.stargatetech2.api.StargateTechAPI;
import lordfokas.stargatetech2.api.bus.IBusDriver;
import lordfokas.stargatetech2.api.bus.IBusInterface;
import net.minecraft.nbt.NBTTagCompound;

public class TileBeaconConsole extends TileBeacon{
	private IBusDriver driver = null;
	private IBusInterface iface = StargateTechAPI.api().getFactory().getIBusInterface(this, driver);
	
	@Override
	protected void readNBT(NBTTagCompound nbt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void writeNBT(NBTTagCompound nbt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IBusInterface[] getInterfaces(int side) {
		return new IBusInterface[]{iface};
	}

}
