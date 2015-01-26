package lordfokas.stargatetech2.modules.transport.beacons;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import lordfokas.stargatetech2.modules.transport.TileBeaconTransceiver;
import lordfokas.stargatetech2.util.Vec3Int;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
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
	private static class RegistryData extends WorldSavedData{
		private HashMap<String, BeaconNetwork> networks = new HashMap();
		
		private RegistryData(String key){
			super(key);
		}
		
		@Override
		public void readFromNBT(NBTTagCompound nbt){
			networks = new HashMap();
			NBTTagList list = nbt.getTagList("networks", 10);
			for(int i = 0; i < list.tagCount(); i++){
				NBTTagCompound tag = list.getCompoundTagAt(i);
				NBTTagCompound net = tag.getCompoundTag("network");
				String name = tag.getString("name");
				BeaconNetwork network = new BeaconNetwork(name);
				network.readFromNBT(net);
				networks.put(name, network);
			}
		}
		
		@Override
		public void writeToNBT(NBTTagCompound nbt){
			NBTTagList list = new NBTTagList();
			for(Entry<String, BeaconNetwork> e : networks.entrySet()){
				NBTTagCompound tag = new NBTTagCompound();
				NBTTagCompound net = new NBTTagCompound();
				
				// If a network is empty, don't save it.
				// This automatically removes unused nets on save/load
				if(!e.getValue().writeToNBT(net)) continue;
				
				tag.setString("name", e.getKey());
				tag.setTag("network", net);
				list.appendTag(tag);
			}
			nbt.setTag("networks", list);
		}
	}
	
	
	
	// BEACON NETWORK // ####################################
	private static class BeaconNetwork{
		private final String networkName;
		private LinkedList<Vec3Int> beacons = new LinkedList();
		
		private BeaconNetwork(String name){
			this.networkName = name;
		}
		
		private void readFromNBT(NBTTagCompound nbt){
			beacons = new LinkedList();
			int size = nbt.getInteger("size");
			for(int i = 0; i < size; i++){
				beacons.add(Vec3Int.fromNBT(nbt.getCompoundTag("b" + i)));
			}
		}
		
		private boolean writeToNBT(NBTTagCompound nbt){
			int size = beacons.size();
			if(size == 0) return false;
			for(int i = 0; i < size; i++){
				nbt.setTag("b" + i, beacons.get(i).toNBT());
			}
			nbt.setInteger("size", size);
			return true;
		}
	}
}
