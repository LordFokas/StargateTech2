package lordfokas.stargatetech2.transport;

import lordfokas.stargatetech2.api.StargateTechAPI;
import lordfokas.stargatetech2.api.bus.IBusDriver;
import lordfokas.stargatetech2.api.bus.IBusInterface;
import net.minecraft.nbt.NBTTagCompound;

public class TileBeaconTransceiver extends TileBeacon{
	private BusDriverTransceiverA antennaDriver = new BusDriverTransceiverA();
	private BusDriverTransceiverB beaconDriver = new BusDriverTransceiverB();
	private IBusInterface antennaInterface = StargateTechAPI.api().getFactory().getIBusInterface(this, antennaDriver);
	private IBusInterface beaconInterface = StargateTechAPI.api().getFactory().getIBusInterface(this, beaconDriver);
	
	@Override
	protected void readNBT(NBTTagCompound nbt) {
		antennaInterface.readFromNBT(nbt, "busInterfaceA");
		beaconInterface.readFromNBT(nbt, "busInterfaceB");
	}

	@Override
	protected void writeNBT(NBTTagCompound nbt) {
		antennaInterface.writeToNBT(nbt, "busInterfaceA");
		beaconInterface.writeToNBT(nbt, "busInterfaceB");
	}

	@Override
	public IBusInterface[] getInterfaces(int side) {
		if(side != 0 && side != 1) return null;
		else return new IBusInterface[]{side == 0 ? beaconInterface : antennaInterface};
	}
}
