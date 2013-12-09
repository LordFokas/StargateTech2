package stargatetech2.core.network.stargate;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;
import stargatetech2.api.stargate.Address;
import stargatetech2.api.stargate.IStargateNetwork;
import stargatetech2.api.stargate.Symbol;
import stargatetech2.common.util.StargateLogger;
import cpw.mods.fml.common.FMLCommonHandler;

public class StargateNetwork implements IStargateNetwork{
	public static final StargateNetwork INSTANCE = new StargateNetwork();
	public static final int RANDOM_ADDRESS_LENGTH = 8;
	
	private boolean isLoaded;
	private HashMap<Integer, DimensionPrefix> prefixes;
	private HashMap<Address, AddressMapping> addresses;
	
	public static StargateNetwork instance(){
		return INSTANCE;
	}
	
	private StargateNetwork(){
		MinecraftForge.EVENT_BUS.register(this);
		isLoaded = false;
	}
	
	@ForgeSubscribe
	public void save(WorldEvent.Save event){
		writeToFile();
	}
	
	public void load(){
		addresses = new HashMap();
		prefixes = new HashMap();
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
	public Address parseAddress(String address){
		return null;
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
	
	private DimensionPrefix generatePrefixForDimension(Integer key){
		DimensionPrefix prefix;
		do{
			prefix = DimensionPrefix.generateRandom();
		}while(prefixes.containsValue(prefix));
		prefixes.put(key, prefix);
		return prefix;
	}
	
	private File getFile(String file){
		return new File(DimensionManager.getCurrentSaveRootDirectory().getAbsolutePath() + File.separator + "SGTech2_" + file);
	}
	
	private void readFromFile(){
		try{
			File addressFile = getFile("addresses.dat");
			File prefixFile = getFile("prefixes.dat");
			boolean hasAddress = addressFile.exists();
			boolean hasPrefix = prefixFile.exists();
			if(hasAddress && hasPrefix){
				readAddresses(addressFile);
				readPrefixes(prefixFile);
			}else if(hasAddress || hasPrefix){
				StargateLogger.severe("Either the Address or Prefix Stargate Network file is missing. This may be a very serious problem!");
				FMLCommonHandler.instance().raiseException(new Exception("StargateTech2 detected save corruption!"), "StargateTech2 detected save corruption!", false);
			}else{
				StargateLogger.warning("Both Address and Prefix Stargate Network files are missing. This is normal when saves are first created.");
				StargateLogger.info("Creating new Address and Prefix files for the Stargate Network.");
				addressFile.createNewFile();
				prefixFile.createNewFile();
			}
		}catch(Exception e){
			StargateLogger.severe("There was an error while trying to read Stargate Network files");
			e.printStackTrace();
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
			File addressFile = getFile("addresses.dat");
			File prefixFile = getFile("prefixes.dat");
			if(!addressFile.exists()){
				StargateLogger.warning("Stargate Network Address file is missing. A new one is being created.");
				addressFile.createNewFile();
			}
			if(!prefixFile.exists()){
				StargateLogger.warning("Stargate Network Prefix file is missing. A new one is being created.");
				prefixFile.createNewFile();
			}
			writeAddresses(addressFile);
			writePrefixes(prefixFile);
			StargateLogger.info("Stargate Network saved successfully.");
		}catch(Exception e){
			StargateLogger.severe("There was an error while trying to write Stargate Network files");
			e.printStackTrace();
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