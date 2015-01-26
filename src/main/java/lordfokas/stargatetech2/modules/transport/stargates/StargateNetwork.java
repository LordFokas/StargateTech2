package lordfokas.stargatetech2.modules.transport.stargates;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

import lordfokas.stargatetech2.api.stargate.Address;
import lordfokas.stargatetech2.api.stargate.DialError;
import lordfokas.stargatetech2.api.stargate.DialEvent;
import lordfokas.stargatetech2.api.stargate.IDynamicWorldLoader;
import lordfokas.stargatetech2.api.stargate.IStargateNetwork;
import lordfokas.stargatetech2.api.stargate.Symbol;
import lordfokas.stargatetech2.modules.transport.TileStargate;
import lordfokas.stargatetech2.util.ChunkLoader;
import lordfokas.stargatetech2.util.ConfigServer;
import lordfokas.stargatetech2.util.Helper;
import lordfokas.stargatetech2.util.StargateLogger;
import lordfokas.stargatetech2.util.api.SeedingShip;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class StargateNetwork implements IStargateNetwork{
	private static final StargateNetwork INSTANCE = new StargateNetwork();
	public static final long MIN_SAVE_INTERVAL_MS = 5000;
	public static final int RANDOM_ADDRESS_LENGTH = 8;
	
	private boolean isLoaded;
	private HashMap<DimensionPrefix, IDynamicWorldLoader> reserved;
	private HashMap<Integer, DimensionPrefix> prefixes;
	private HashMap<Address, AddressMapping> addresses;
	private ArrayList<Wormhole> activeWormholes;
	private LinkedList<IDynamicWorldLoader> loaders;
	private Address dynamicLoadingAddr = null;
	private DimensionPrefix dynamicLoadingPrefix = null;
	private long saveTime;
	
	public static StargateNetwork instance(){
		return INSTANCE;
	}
	
	private StargateNetwork(){
		MinecraftForge.EVENT_BUS.register(this);
		isLoaded = false;
		saveTime = 0;
	}
	
	@SubscribeEvent
	public void save(WorldEvent.Save event){
		long newSaveTime = System.currentTimeMillis();
		if(newSaveTime > saveTime + MIN_SAVE_INTERVAL_MS){
			saveTime = newSaveTime;
			writeToFile();
		}
	}
	
	public void load(){
		reserved = new HashMap();
		addresses = new HashMap();
		prefixes = new HashMap();
		activeWormholes = new ArrayList();
		loaders = new LinkedList();
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
	
	@Override
	public void registerDynamicWorldLoader(IDynamicWorldLoader dwl) {
		if(!loaders.contains(dwl)) loaders.add(dwl);
	}

	@Override
	public void unregisterDynamicWorldLoader(IDynamicWorldLoader dwl) {
		loaders.remove(dwl);
	}
	
	private void dinamicallyLoadWorlds(AddressMapping dstmap, AddressMapping srcmap, Address destination){
		Symbol[] syms = new Symbol[]{destination.getSymbol(0), destination.getSymbol(1), destination.getSymbol(2)};
		dynamicLoadingPrefix = new DimensionPrefix(syms);
		dynamicLoadingAddr = destination;
		if(srcmap != null && dstmap == null && !prefixes.containsValue(dynamicLoadingPrefix)){
			if(reserved.containsKey(dynamicLoadingPrefix)){
				IDynamicWorldLoader loader = reserved.get(dynamicLoadingPrefix);
				loader.loadWorldFor(destination, SeedingShip.SHIP);
			}else{
				Collections.shuffle(loaders);
				for(IDynamicWorldLoader loader : loaders){
					if(loader.willCreateWorldFor(destination)){
						loader.loadWorldFor(destination, SeedingShip.SHIP);
						break;
					}
				}
			}
		}
		dynamicLoadingAddr = null;
		dynamicLoadingPrefix = null;
	}
	
	public DialError dial(Address source, Address destination, int timeout){
		if (MinecraftForge.EVENT_BUS.post(new DialEvent.Pre(source, destination, timeout))) return DialError.DIALING_EVENT_CANCELED;
		DialError error = DialError.UNKNOWN_LOGIC_ERROR;
		AddressMapping srcmap = addresses.get(source);
		AddressMapping dstmap = addresses.get(destination);
		dinamicallyLoadWorlds(dstmap, srcmap, destination);
		dstmap = addresses.get(destination);
		if(srcmap != null && dstmap != null){
			WorldServer srcworld = MinecraftServer.getServer().worldServerForDimension(srcmap.getDimension());
			WorldServer dstworld = MinecraftServer.getServer().worldServerForDimension(dstmap.getDimension());
			if(srcworld != null && dstworld != null && srcworld != dstworld){
				long srcChunks = ChunkLoader.load9Chunks(srcworld, srcmap.getXCoord() >> 4, srcmap.getZCoord() >> 4);
				long dstChunks = ChunkLoader.load9Chunks(dstworld, dstmap.getXCoord() >> 4, dstmap.getZCoord() >> 4);
				if(srcChunks >= 0 && dstChunks >= 0){
					TileEntity srcte = srcworld.getTileEntity(srcmap.getXCoord(), srcmap.getYCoord(), srcmap.getZCoord());
					TileEntity dstte = dstworld.getTileEntity(dstmap.getXCoord(), dstmap.getYCoord(), dstmap.getZCoord());
					if(srcte instanceof TileStargate && dstte instanceof TileStargate){
						TileStargate src = (TileStargate) srcte;
						TileStargate dst = (TileStargate) dstte;
						if(src.canDial(8) && !dst.hasActiveWormhole()){
							activeWormholes.add(new Wormhole(src, dst, srcChunks, dstChunks, timeout));
							MinecraftForge.EVENT_BUS.post(new DialEvent.Success(source, destination, timeout));
							return DialError.SUCCESSFULLY_DIALED;
						}else{
							if(dst.hasActiveWormhole()) error = DialError.TARGET_GATE_BUSY;
							else error = DialError.NOT_ENOUGH_POWER;
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
	
	@Override
	public boolean reserveDimensionPrefix(IDynamicWorldLoader dwl, Symbol[] syms) {
		if(dwl == null) throw new IllegalArgumentException("The IDynamicWorldLoader must not be null!");
		if(syms == null) throw new IllegalArgumentException("The prefix must not be null!");
		if(syms.length != 3) throw new IllegalArgumentException("The prefix must contain exactly 3 Symbols!");
		for(Symbol s : syms){
			if(s == null || s == Symbol.VOID) throw new IllegalArgumentException("The symbols must not be null or Symbol.VOID!");
		}
		
		DimensionPrefix prefix = new DimensionPrefix(syms);
		if(!reserved.containsKey(prefix)){
			reserved.put(prefix, dwl);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean prefixExists(Symbol[] syms) {
		if(syms == null) throw new IllegalArgumentException("The prefix must not be null!");
		if(syms.length != 3) throw new IllegalArgumentException("The prefix must contain exactly 3 Symbols!");
		for(Symbol s : syms){
			if(s == null || s == Symbol.VOID) throw new IllegalArgumentException("The symbols must not be null or Symbol.VOID!");
		}
		
		DimensionPrefix prefix = new DimensionPrefix(syms);
		return reserved.containsKey(prefix) || prefixes.containsValue(prefix);
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
	
	public AddressMapping getAddressMapping(Address address) {
		return addresses.get(address);
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
			if(dynamicLoadingAddr != null && dynamicLoadingPrefix != null && prefixes.get(world.provider.dimensionId) == null){
				prefixes.put(world.provider.dimensionId, dynamicLoadingPrefix);
				address = dynamicLoadingAddr;
			}else{
				address = getRandomAddress(world);
			}
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
		int nearest = r < 0 ? -1 : r*r;
		Address addr = null;
		for(AddressMapping map : addresses.values()){
			if(map.getDimension() == dim){
				int dx = x - map.getXCoord();
				int dy = y - map.getYCoord();
				int dz = z - map.getZCoord();
				int dst = dx*dx + dy*dy + dz*dz;
				if (dst < nearest || nearest < 0) {
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
				}while(prefixes.containsValue(prefix) || reserved.containsKey(prefix));
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
				StargateLogger.error("Some Stargate Network data files are missing. This may be a very serious problem!");
				FMLCommonHandler.instance().raiseException(new Exception("StargateTech2 detected save corruption!"), "StargateTech2 detected save corruption!", false);
			}else{
				StargateLogger.warning("All Stargate Network data files are missing. This is normal when saves are first created.");
				StargateLogger.info("Creating new Wormhole, Address and Prefix files for the Stargate Network.");
				wormholeFile.createNewFile();
				addressFile.createNewFile();
				prefixFile.createNewFile();
			}
		}catch(Exception e){
			StargateLogger.error("There was an error while trying to read Stargate Network files");
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
			StargateLogger.error("There was an error while trying to write Stargate Network files");
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