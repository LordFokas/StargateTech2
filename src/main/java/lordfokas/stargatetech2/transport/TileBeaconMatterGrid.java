package lordfokas.stargatetech2.transport;

import lordfokas.stargatetech2.api.StargateTechAPI;
import lordfokas.stargatetech2.api.bus.IBusDriver;
import lordfokas.stargatetech2.api.bus.IBusInterface;
import net.minecraft.nbt.NBTTagCompound;

public class TileBeaconMatterGrid extends TileBeacon {
	private IBusDriver driver = null;
	private IBusInterface iface = StargateTechAPI.api().getFactory().getIBusInterface(this, driver);
	
	@Override
	protected void readNBT(NBTTagCompound nbt) {
		iface.readFromNBT(nbt, "interface");
	}

	@Override
	protected void writeNBT(NBTTagCompound nbt) {
		iface.writeToNBT(nbt, "interface");
	}
	
	@Override
	public IBusInterface[] getInterfaces(int side) {
		return side == 0 ? new IBusInterface[]{iface} : null;
	}
}