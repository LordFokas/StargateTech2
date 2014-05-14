package stargatetech2.transport.stargates;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;
import stargatetech2.api.stargate.Address;
import stargatetech2.api.stargate.DialError;
import stargatetech2.api.stargate.DialEvent;
import stargatetech2.api.stargate.IStargateNetwork;
import stargatetech2.api.stargate.Symbol;
import stargatetech2.core.util.ChunkLoader;
import stargatetech2.core.util.ConfigServer;
import stargatetech2.core.util.Helper;
import stargatetech2.core.util.StargateLogger;
import stargatetech2.transport.tileentity.TileStargate;
import cpw.mods.fml.common.FMLCommonHandler;

public class StargateNetwork implements IStargateNetwork{
	private static final StargateNetwork INSTANCE = new StargateNetwork();
	public static final long MIN_SAVE_INTERVAL_MS = 5000;
	public static final int RANDOM_ADDRESS_LENGTH = 8;
	
	private boolean isLoaded;
	private HashMap<Integer, DimensionPrefix> prefixes;
	private HashMap<Address, AddressMapping> addresses;
	private ArrayList<Wormhole> activeWormholes;
	private long saveTime;
	
	public static StargateNetwork instance(){
		return INSTANCE;
	}
	
	private StargateNetwork(){
		MinecraftForge.EVENT_BUS.register(this);
		isLoaded = false;
		saveTime = 0;
	}
	
	@ForgeSubscribe
	public void save(WorldEvent.Save event){
		long newSaveTime = System.currentTimeMillis();
		if(newSaveTime > saveTime + MIN_SAVE_INTERVAL_MS){
			saveTime = newSaveTime;
			writeToFile();
		}
	}
	
	public void load(){
		addresses = new HashMap();
		prefixes = new HashMap();
		activeWormholes = new ArrayList();
		readFromFile();
		isLoaded = true;
	}
	
	public void unload(){
		isLoaded = false;
	}
	
	@Override
	public boolean isLoaded(){
		return isLoaded;
	}
	
	public DialError dial(Address source, Address destination, int timeout){
		if (!MinecraftForge.EVENT_BUS.post(new DialEvent.Pre(source, destination, timeout))) return null;
		
		DialError error = null;
		AddressMapping srcmap = addresses.get(source);
		AddressMapping dstmap = addresses.get(destination);
		if(srcmap != null && dstmap != null){
			WorldServer srcworld = MinecraftServer.getServer().worldServerForDimension(srcmap.getDimension());
			WorldServer dstworld = MinecraftServer.getServer().worldServerForDimension(dstmap.getDimension());
			if(srcworld != null && dstworld != null && srcworld != dstworld){
				long srcChunks = ChunkLoader.load9Chunks(srcworld, srcmap.getXCoord() >> 4, srcmap.getZCoord() >> 4);
				long dstChunks = ChunkLoader.load9Chunks(dstworld, dstmap.getXCoord() >> 4, dstmap.getZCoord() >> 4);
				if(srcChunks >= 0 && dstChunks >= 0){
					TileEntity srcte = srcworld.getBlockTileEntity(srcmap.getXCoord(), srcmap.getYCoord(), srcmap.getZCoord());
					TileEntity dstte = dstworld.getBlockTileEntity(dstmap.getXCoord(), dstmap.getYCoord(), dstmap.getZCoord());
					if(srcte instanceof TileStargate && dstte instanceof TileStargate){
						TileStargate src = (TileStargate) srcte;
						TileStargate dst = (TileStargate) dstte;
						if(src.canDial(8) && !dst.hasActiveWormhole()){
							activeWormholes.add(new Wormhole(src, dst, srcChunks, dstChunks, timeout));
							MinecraftForge.EVENT_BUS.post(new DialEvent.Success(source, destination, timeout));
							return null;
						}else{
							if(dst.hasActiveWormhole()) error = DialError.TARGET_GATE_BUSY;
							else error = DialError.SOURCE_GATE_UNABLE_TO_DIAL;
						}
					}else{
						if(!(srcte instanceof TileStargate)) error = DialError.SOURCE_GATE_NOT_FOUND;
						else error = DialError.TARGET_GATE_NOT_FOUND;
					}
				}else{
					if(srcChunks < 0) error = DialError.FAILED_CHUNKLOADING_SOURCE;
					else error = DialError.FAILED_CHUNKLOADING_TARGET;
				}
				ChunkLoader.release(srcChunks);
				ChunkLoader.release(dstChunks);
			}else{
				if(srcworld == null) error = DialError.SOURCE_WORLD_NOT_FOUND;
				else if(dstworld == null) error = DialError.TARGET_WORLD_NOT_FOUND;
				else if(dstworld == srcworld) error = DialError.CANNOT_DIAL_SAME_WORLD;
			}
		}else{
			if(srcmap == null) error = DialError.SOURCE_ADDRESS_NOT_FOUND;
			else error = DialError.TARGET_ADDRESS_NOT_FOUND;
		}
		MinecraftForge.EVENT_BUS.post(new DialEvent.Error(source, destination, error));
		return error;
	}
	
	public void removeWormhole(Wormhole wormhole){
		activeWormholes.remove(wormhole);
	}
	
	public boolean canPlaceStargateAt(World w, int x, int y, int z){
		if(!isLoaded) return false;
		int dim = w.provider.dimensionId;
		for(AddressMapping map : addresses.values()){
			if(map.getDimension() == dim){
				int dx = x - map.getXCoord();
				int dy = y - map.getYCoord();
				int dz = z - map.getZCoord();
				if(dx*dx + dy*dy + dz*dz < ConfigServer.stargateMinDistance){
					return false;
				}
			}
		}
		return true;
	}
	
	@Override
	public Address parseAddress(String address){
		return parse(address);
	}
	
	public static Address parse(String address){
		try{
			String[] parts = address.toLowerCase().split(" ");
			LinkedList<Symbol> symbols = new LinkedList();
			if(parts.length != 3) throw new Exception("Address too short.");
			for(String part : parts){
				for(int p = 0; p < 3; p++){
					for(int s = 1; s < Symbol.values().length; s++){
						Symbol sym = Symbol.get(s);
						String name = sym.toString().toLowerCase();
						if(part.contentEquals(name)){
							symbols.addLast(sym);
							p = 5;
							break;
						}else if(part.startsWith(name)){
							symbols.addLast(sym);
							part = part.substring(name.length());
							break;
						}
					}
				}
			}
			Symbol[] sym = new Symbol[symbols.size()];
			for(int i = 0; i < symbols.size(); i++){
				sym[i] = symbols.get(i);
			}
			return Address.create(sym);
		}catch(Exception e){
			return null;
		}
	}
	
	@Override
	public boolean addressExists(Address address){
		if(isLoaded) return addresses.containsKey(address);
		else return false;
	}
	
	private Address getRandomAddress(World world){
		Random random = new Random();
		Symbol[] symbols;
		Address address;
		DimensionPrefix prefix;
		Integer key = new Integer(world.provider.dimensionId);
		if(prefixes.containsKey(key)){
			prefix = prefixes.get(key);
		}else{
			prefix = generatePrefixForDimension(key);
		}
		Symbol[] pfx = prefix.getSymbols();
		do{
			boolean used[] = new boolean[40];
			used[0] = true;
			symbols = new Symbol[RANDOM_ADDRESS_LENGTH];
			for(int i = 0; i < symbols.length; i++){
				if(i < pfx.length){
					symbols[i] = pfx[i];
					used[pfx[i].ordinal()] = true;
				}else{
					int s;
					do{
						s = random.nextInt(39) + 1;
					}while(used[s]);
					symbols[i] = Symbol.values()[s];
					used[s] = true;
				}
			}
			address = Address.create(symbols);
		}while(address == null || addressExists(address));
		return address;
	}
	
	@Override
	public Address getAddressOf(World world, int x, int y, int z){
		if(isLoaded){
			Collection<AddressMapping> mappings = addresses.values();
			for(AddressMapping m : mappings){
				if(m.getDimension() == world.provider.dimensionId && m.getXCoord() == x && m.getYCoord() == y && m.getZCoord() == z){
					return m.getAddress();
				}
			}
		}
		return null;
	}
	
	public Address getMyAddress(World world, int x, int y, int z){
		Address address = getAddressOf(world, x, y, z);
		if(address == null){
			address = getRandomAddress(world);
			AddressMapping mapping = new AddressMapping(address, world.provider.dimensionId, x, y, z);
			addresses.put(address, mapping);
		}
		return address;
	}
	
	public void freeMyAddress(World world, int x, int y, int z){
		Address address = getAddressOf(world, x, y, z);
		if(address != null){
			addresses.remove(address);
		}
	}
	
	@Override
	public Address findNearestStargate(World w, int x, int y, int z, int r) {
		if(!isLoaded) return null;
		int dim = w.provider.dimensionId;
		boolean fullScan = r < 0;
		int nearest = r*r;
		Address addr = null;
		for(AddressMapping map : addresses.values()){
			if(map.getDimension() == dim){
				int dx = x - map.getXCoord();
				int dy = y - map.getYCoord();
				int dz = z - map.getZCoord();
				int dst = dx*dx + dy*dy + dz*dz;
				
				if (dst < nearest || fullScan) {
					nearest = dst;
					addr = map.getAddress();
				}
			}
		}
		return addr;
	}
	
	private DimensionPrefix generatePrefixForDimension(Integer key){
		DimensionPrefix prefix;
		switch(key.intValue()){
			case 0:
				prefix = new DimensionPrefix(LoreAddresses.OVERWORLD);
				break;
			case -1:
				prefix = new DimensionPrefix(LoreAddresses.NETHER);
				break;
			default:
				do{
					prefix = DimensionPrefix.generateRandom();
				}while(prefixes.containsValue(prefix));
		}
		prefixes.put(key, prefix);
		return prefix;
	}
	
	public void initializeWormholes(){
		if(!isLoaded) return;
		for(Wormhole wormhole : activeWormholes){
			wormhole.initialize();
		}
	}
	
	private void readFromFile(){
		try{
			File wormholeFile = Helper.getFile("wormholes.dat");
			File addressFile = Helper.getFile("addresses.dat");
			File prefixFile = Helper.getFile("prefixes.dat");
			boolean hasWormhole = wormholeFile.exists();
			boolean hasAddress = addressFile.exists();
			boolean hasPrefix = prefixFile.exists();
			if(hasWormhole && hasAddress && hasPrefix){
				readWormholes(wormholeFile);
				readAddresses(addressFile);
				readPrefixes(prefixFile);
			}else if(hasWormhole || hasAddress || hasPrefix){
				StargateLogger.severe("Some Stargate Network data files are missing. This may be a very serious problem!");
				FMLCommonHandler.instance().raiseException(new Exception("StargateTech2 detected save corruption!"), "StargateTech2 detected save corruption!", false);
			}else{
				StargateLogger.warning("All Stargate Network data files are missing. This is normal when saves are first created.");
				StargateLogger.info("Creating new Wormhole, Address and Prefix files for the Stargate Network.");
				wormholeFile.createNewFile();
				addressFile.createNewFile();
				prefixFile.createNewFile();
			}
		}catch(Exception e){
			StargateLogger.severe("There was an error while trying to read Stargate Network files");
			e.printStackTrace();
		}
	}
	
	private void readWormholes(File wormholeFile) throws Exception{
		FileInputStream fis = null;
		DataInputStream dis = null;
		try{
			fis = new FileInputStream(wormholeFile);
			dis = new DataInputStream(fis);
			int count = dis.readInt();
			for(int i = 0; i < count; i++){
				activeWormholes.add(Wormhole.readFromStream(dis));
			}
			dis.close();
		}catch(Exception e){
			try{ dis.close(); }
			catch(Exception ignored){}
			throw e;
		}
	}
	
	private void readAddresses(File addressFile) throws Exception{
		FileInputStream fis = null;
		DataInputStream dis = null;
		try{
			fis = new FileInputStream(addressFile);
			dis = new DataInputStream(fis);
			int count = dis.readInt();
			for(int i = 0; i < count; i++){
				AddressMapping address = AddressMapping.readFromStream(dis);
				addresses.put(address.getAddress(), address);
			}
			dis.close();
		}catch(Exception e){
			try{ dis.close(); }
			catch(Exception ignored){}
			throw e;
		}
	}
	
	private void readPrefixes(File prefixFile) throws Exception{
		FileInputStream fis = null;
		DataInputStream dis = null;
		try{
			fis = new FileInputStream(prefixFile);
			dis = new DataInputStream(fis);
			int count = dis.readInt();
			for(int i = 0; i < count; i++){
				int d = dis.readInt();
				Symbol[] symbols = new Symbol[]{
					Symbol.get(dis.readInt()),
					Symbol.get(dis.readInt()),
					Symbol.get(dis.readInt())
				};
				prefixes.put(new Integer(d), new DimensionPrefix(symbols));
			}
			dis.close();
		}catch(Exception e){
			try{ dis.close(); }
			catch(Exception ignored){}
			throw e;
		}
	}
	
	private void writeToFile(){
		try{
			File wormholeFile = Helper.getFile("wormholes.dat");
			File addressFile = Helper.getFile("addresses.dat");
			File prefixFile = Helper.getFile("prefixes.dat");
			if(!wormholeFile.exists()){
				StargateLogger.warning("Stargate Network Wormhole file is missing. A new one is being created.");
				wormholeFile.createNewFile();
			}
			if(!addressFile.exists()){
				StargateLogger.warning("Stargate Network Address file is missing. A new one is being created.");
				addressFile.createNewFile();
			}
			if(!prefixFile.exists()){
				StargateLogger.warning("Stargate Network Prefix file is missing. A new one is being created.");
				prefixFile.createNewFile();
			}
			writeWormholes(wormholeFile);
			writeAddresses(addressFile);
			writePrefixes(prefixFile);
		}catch(Exception e){
			StargateLogger.severe("There was an error while trying to write Stargate Network files");
			e.printStackTrace();
		}
	}
	
	private void writeWormholes(File wormholeFile) throws Exception{
		FileOutputStream fos = null;
		DataOutputStream dos = null;
		try{
			fos = new FileOutputStream(wormholeFile, false);
			dos = new DataOutputStream(fos);
			dos.writeInt(activeWormholes.size());
			for(Wormhole wormhole : activeWormholes){
				wormhole.writeToStream(dos);
			}
			dos.close();
		}catch(Exception e){
			try{ dos.close(); }
			catch(Exception ignored){}
			throw e;
		}
	}
	
	private void writeAddresses(File addressFile) throws Exception{
		FileOutputStream fos = null;
		DataOutputStream dos = null;
		try{
			fos = new FileOutputStream(addressFile, false);
			dos = new DataOutputStream(fos);
			Collection<AddressMapping> values = addresses.values();
			dos.writeInt(values.size());
			for(AddressMapping address : values){
				address.writeToStream(dos);
			}
			dos.close();
		}catch(Exception e){
			try{ dos.close(); }
			catch(Exception ignored){}
			throw e;
		}
	}
	
	private void writePrefixes(File prefixFile) throws Exception{
		FileOutputStream fos = null;
		DataOutputStream dos = null;
		try{
			fos = new FileOutputStream(prefixFile, false);
			dos = new DataOutputStream(fos);
			Set<Integer> keys = prefixes.keySet();
			dos.writeInt(keys.size());
			for(Integer key : keys){
				DimensionPrefix prefix = prefixes.get(key);
				Symbol[] symbols = prefix.getSymbols();
				dos.writeInt(key.intValue());
				for(Symbol s : symbols){
					dos.writeInt(s.ordinal());
				}
			}
			dos.close();
		}catch(Exception e){
			try{ dos.close(); }
			catch(Exception ignored){}
			throw e;
		}
	}
}