package lordfokas.stargatetech2.modules.automation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import lordfokas.stargatetech2.api.bus.BusPacket;
import lordfokas.stargatetech2.api.bus.BusPacketNetScan;
import lordfokas.stargatetech2.api.bus.BusPacketNetScan.Device;
import lordfokas.stargatetech2.api.bus.BusProtocols;
import lordfokas.stargatetech2.api.bus.IBusDevice;
import lordfokas.stargatetech2.api.bus.IBusDriver;
import lordfokas.stargatetech2.api.bus.IBusInterface;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public final class BusInterface implements IBusInterface{
	private static final byte BROADCAST		= (byte) 0xFF;
	private static final byte PROMISCUOUS	= (byte) 0x00;
	
	private final Set<RemoteDevice>[] addressingTable;
	private Set<RemoteDevice> addressCache;
	private final IBusDevice device;
	private final IBusDriver driver;
	
	public BusInterface(IBusDevice device, IBusDriver driver){
		this.device = device;
		this.driver = driver;
		addressingTable = new Set[6];
		addressCache = new HashSet();
	}
	
	public void setAddressingTable(EnumFacing side, Set<RemoteDevice> table){
		addressingTable[side.ordinal()] = table;
		rebuildAddressCache();
	}
	
	private void rebuildAddressCache(){
		addressCache = new HashSet();
		for(Set<RemoteDevice> table : addressingTable)
			if(table != null) addressCache.addAll(table);
	}

	@Override
	public void sendAllPackets(){
		if(driver.isInterfaceEnabled()){
			BusPacket packet;
			while((packet = driver.getNextPacketToSend()) != null){
				sendPacket(packet);
			}
		}
	}
	
	private void sendPacket(BusPacket packet){
		ArrayList memory = new ArrayList();
		World w = device.getWorld();
		for(RemoteDevice device : addressCache){
			TileEntity te = w.getTileEntity(device.pos);
			if(te instanceof IBusDevice){
				IBusInterface[] interfaces = ((IBusDevice)te).getInterfaces(device.side);
				if(interfaces == null){
					continue;
				}
				for(IBusInterface i : interfaces){
					if(i instanceof BusInterface && !memory.contains(i)){
						((BusInterface)i).recvPacket(packet);
						memory.add(i);
					}
				}
			}
		}
	}
	
	public void recvPacket(BusPacket packet){
		boolean mapping = packet.getProtocolID() == BusProtocols.PROTOCOL_NETSCAN;
		if(!mapping && !driver.isInterfaceEnabled()) return;
		// split addresses into net and host address.
		// uses /8 netmask in a 16-bit address.
		short address = driver.getInterfaceAddress();
		byte addrNet  = (byte)((address & 0xFF00) >> 8);
		byte addrHost = (byte) (address & 0x00FF);
		short sender  = packet.getTarget();
		byte sendNet  = (byte)((sender & 0xFF00) >> 8);
		byte sendHost = (byte) (sender & 0x00FF);
		
		// if addresses match      or it's a broadcast      or we're sniffing
		if(sendNet  == addrNet  || sendNet  == BROADCAST || addrNet  == PROMISCUOUS)
		if(sendHost == addrHost || sendHost == BROADCAST || addrHost == PROMISCUOUS){
			if(mapping){
				String dsc = driver.getDescription();
				String snm = driver.getShortName();
				short adrs = driver.getInterfaceAddress();
				boolean on = driver.isInterfaceEnabled();
				((BusPacketNetScan)packet).addDevice(new Device(dsc, snm, adrs, on, device.getXCoord(), device.getYCoord(), device.getZCoord()));
			}else if(driver.canHandlePacket(sender, packet.getProtocolID(), packet.hasPlainText())){
				driver.handlePacket(packet);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt, String tag){
		NBTTagCompound data = new NBTTagCompound();
		for(int i = 0; i < addressingTable.length; i++){
			NBTTagCompound table = new NBTTagCompound();
			Set<RemoteDevice> tbl = addressingTable[i];
			if(tbl == null){
				table.setInteger("size", 0);
			}else{
				table.setInteger("size", tbl.size());
				int j = 0;
				for(RemoteDevice rdev : tbl){
					table.setTag("addr"+j, rdev.serializeNBT());
				}
			}
			data.setTag("table"+i, table);
		}
		nbt.setTag(tag, data);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt, String tag){
		NBTTagCompound data = nbt.getCompoundTag(tag);
		for(int i = 0; i < addressingTable.length; i++){
			NBTTagCompound table = data.getCompoundTag("table"+i);
			Set<RemoteDevice> tbl = null;
			int sz = table.getInteger("size");
			if(sz != 0){
				tbl = new HashSet();
				for(int j = 0; j < sz; j++){
					RemoteDevice device = new RemoteDevice(null, null);
					device.deserializeNBT(table.getCompoundTag("addr"+j));
					tbl.add(device);
				}
			}
			addressingTable[i] = tbl;
		}
		rebuildAddressCache();
	}
	
	public short getAddress(){
		return driver.getInterfaceAddress();
	}
}