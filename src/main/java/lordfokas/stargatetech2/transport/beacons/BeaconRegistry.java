package lordfokas.stargatetech2.transport.beacons;

import java.util.HashMap;
import java.util.LinkedList;

import lordfokas.stargatetech2.core.Vec3Int;
import lordfokas.stargatetech2.transport.TileBeaconTransceiver;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.WorldServer;

public class BeaconRegistry{
	// STATIC CONTEXT // ####################################
	private static final String WORLD_DATA_KEY = "StargateTech2:BeaconRegistry";
	private static HashMap<Integer, BeaconRegistry> registries = new HashMap();
	
	public synchronized static BeaconRegistry forWorld(World world){
		if(!(world instanceof WorldServer)) return null;
		if(registries.containsKey(world.provider.dimensionId)){
			return registries.get(world.provider.dimensionId);
		}else{
			BeaconRegistry registry = new BeaconRegistry((WorldServer)world);
			registries.put(world.provider.dimensionId, registry);
			return registry;
		}
	}
	
	
	
	// OBJECT CONTEXT // ####################################
	private RegistryData registry;
	private int dimension;
	
	private BeaconRegistry(WorldServer world){
		this.dimension = world.provider.dimensionId;
		this.registry = (RegistryData) world.perWorldStorage.loadData(RegistryData.class, WORLD_DATA_KEY);
		if(registry == null) registry = new RegistryData(WORLD_DATA_KEY);
	}
	
	private synchronized BeaconNetwork getNetwork(String network){
		BeaconNetwork beaconNetwork = registry.networks.get(network);
		if(beaconNetwork == null){
			beaconNetwork = new BeaconNetwork(network);
			registry.networks.put(network, beaconNetwork);
		}
		return beaconNetwork;
	}
	
	public LinkedList<BeaconData> getBeaconsInNetwork(String network){
		BeaconNetwork beaconNetwork = getNetwork(network);
		LinkedList<BeaconData> datum = new LinkedList();
		WorldServer w = MinecraftServer.getServer().worldServerForDimension(dimension);
		for(Vec3Int pos : beaconNetwork.beacons){
			TileEntity te = w.getTileEntity(pos.x, pos.y, pos.z);
			if(te instanceof TileBeaconTransceiver){
				TileBeaconTransceiver transceiver = (TileBeaconTransceiver)te;
				if(transceiver.getConnectionMode() == TileBeaconTransceiver.CON_SUBSPACE){
					datum.add(transceiver.getData());
				}
			}
		}
		return datum;
	}
	
	public void addToNetwork(String network, TileBeaconTransceiver beacon){
		BeaconNetwork beaconNetwork = getNetwork(network);
		Vec3Int pos = new Vec3Int(beacon.xCoord, beacon.yCoord, beacon.zCoord);
		if(!beaconNetwork.beacons.contains(pos)){
			beaconNetwork.beacons.add(pos);
		}
	}
	
	public void removeFromNetwork(String network, TileBeaconTransceiver beacon){
		BeaconNetwork beaconNetwork = getNetwork(network);
		Vec3Int pos = new Vec3Int(beacon.xCoord, beacon.yCoord, beacon.zCoord);
		beaconNetwork.beacons.remove(pos);
	}
	
	
	
	// REGISTRY DATA // #####################################
	public static class RegistryData extends WorldSavedData{
		public HashMap<String, BeaconNetwork> networks = new HashMap();
		
		public RegistryData(String key){
			super(key);
		}
		
		@Override
		public void readFromNBT(NBTTagCompound nbt){
			
		}
		
		@Override
		public void writeToNBT(NBTTagCompound nbt){
			
		}
	}
	
	
	
	// BEACON NETWORK // ####################################
	public static class BeaconNetwork{
		public final String networkName;
		private LinkedList<Vec3Int> beacons = new LinkedList();
		
		public BeaconNetwork(String name){
			this.networkName = name;
		}
		
		public void readFromNBT(NBTTagCompound nbt){
			
		}
		
		public void writeToNBT(NBTTagCompound nbt){
			
		}
	}
}
