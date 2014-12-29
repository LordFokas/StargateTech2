package lordfokas.stargatetech2.transport;

import java.util.LinkedList;

import lordfokas.stargatetech2.ModuleAutomation;
import lordfokas.stargatetech2.ModuleTransport;
import lordfokas.stargatetech2.api.StargateTechAPI;
import lordfokas.stargatetech2.api.bus.IBusInterface;
import lordfokas.stargatetech2.transport.beacons.BeaconData;
import lordfokas.stargatetech2.transport.bus.BusDriverTransceiverA;
import lordfokas.stargatetech2.transport.bus.BusDriverTransceiverB;
import net.minecraft.nbt.NBTTagCompound;

public class TileBeaconTransceiver extends TileBeacon{
	private static int _counter_ = 0xF0000000;
	private final String TRANSCEIVER_ID = "BT-" + Integer.toHexString(++_counter_).toUpperCase();
	private final int CON_NONE = 0, CON_BUS = 1, CON_SUBSPACE = 2; 
	
	private BusDriverTransceiverA antennaDriver = new BusDriverTransceiverA();
	private BusDriverTransceiverB beaconDriver = new BusDriverTransceiverB(this);
	private IBusInterface antennaInterface = StargateTechAPI.api().getFactory().getIBusInterface(this, antennaDriver);
	private IBusInterface beaconInterface = StargateTechAPI.api().getFactory().getIBusInterface(this, beaconDriver);
	
	public BeaconData findBeacon(String beaconID){
		LinkedList<BeaconData> beacons = findAllBeacons();
		for(BeaconData beacon : beacons){
			if(beacon.beaconID.equals(beaconID)) return beacon;
		}
		return null;
	}
	
	public LinkedList<BeaconData> findAllBeacons(){
		LinkedList<BeaconData> beacons = new LinkedList();
		
		return beacons;
	}
	
	public BeaconData getData(){
		return new BeaconData(this, TRANSCEIVER_ID, 0xFFFFFF, 0);
	}
	
	public int isConnected(){
		if(worldObj.getBlock(xCoord, yCoord+1, zCoord) == ModuleAutomation.busCable) return CON_BUS;
		for(int i = 1; i <= 3; i++){
			boolean block = worldObj.getBlock(xCoord, yCoord+i, zCoord) == ModuleTransport.beacon;
			boolean meta = worldObj.getBlockMetadata(xCoord, yCoord+i, zCoord) == BlockBeacon.META_ANTENNA;
			if(!(block && meta)) return CON_NONE;
		}
		return CON_SUBSPACE;
	}
	
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
