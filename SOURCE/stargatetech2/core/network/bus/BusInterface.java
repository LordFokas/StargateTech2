package stargatetech2.core.network.bus;

import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import stargatetech2.api.bus.BusPacket;
import stargatetech2.api.bus.IBusDevice;
import stargatetech2.api.bus.IBusDriver;
import stargatetech2.api.bus.IBusInterface;
import stargatetech2.common.util.Vec4Int;

public final class BusInterface implements IBusInterface{
	private static final byte BROADCAST		= (byte) 0xFF;
	private static final byte PROMISCUOUS	= (byte) 0x00;
	
	private final ArrayList<Vec4Int>[] addressingTable;
	private ArrayList<Vec4Int> addressCache;
	private final IBusDevice device;
	private final IBusDriver driver;
	
	public BusInterface(IBusDevice device, IBusDriver driver){
		this.device = device;
		this.driver = driver;
		addressingTable = new ArrayList[6];
		addressCache = new ArrayList();
	}
	
	public void setAddressingTable(int side, ArrayList<Vec4Int> table){
		addressingTable[side] = table;
		rebuildAddressCache();
	}
	
	private void rebuildAddressCache(){
		addressCache = new ArrayList();
		for(int i = 0; i < addressingTable.length; i++){
			ArrayList<Vec4Int> table = addressingTable[i];
			if(table != null){
				for(Vec4Int address : table){
					if(!addressCache.contains(address)){
						addressCache.add(address);
					}
				}
			}
		}
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
		World w = device.getWorldObj();
		for(Vec4Int addr : addressCache){
			TileEntity te = w.getBlockTileEntity(addr.x, addr.y, addr.z);
			if(te instanceof IBusDevice){
				IBusInterface[] interfaces = ((IBusDevice)te).getInterfaces(addr.w);
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
		if(!driver.isInterfaceEnabled()) return;
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
			if(driver.canHandlePacket(sender, packet.getProtocolID(), packet.hasPlainText())){
				driver.handlePacket(packet);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt, String tag){
		NBTTagCompound data = new NBTTagCompound();
		for(int i = 0; i < addressingTable.length; i++){
			NBTTagCompound table = new NBTTagCompound();
			ArrayList<Vec4Int> tbl = addressingTable[i];
			if(tbl == null){
				table.setInteger("size", 0);
			}else{
				table.setInteger("size", tbl.size());
				for(int j = 0; j < tbl.size(); j++){
					table.setCompoundTag("addr"+j, tbl.get(j).toNBT());
				}
			}
			data.setCompoundTag("table"+i, table);
		}
		nbt.setCompoundTag(tag, data);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt, String tag){
		NBTTagCompound data = nbt.getCompoundTag(tag);
		for(int i = 0; i < addressingTable.length; i++){
			NBTTagCompound table = data.getCompoundTag("table"+i);
			ArrayList<Vec4Int> tbl = null;
			int sz = table.getInteger("size");
			if(sz != 0){
				tbl = new ArrayList(sz);
				for(int j = 0; j < sz; j++){
					tbl.add(Vec4Int.fromNBT(table.getCompoundTag("addr"+j)));
				}
			}
			addressingTable[i] = tbl;
		}
		rebuildAddressCache();
	}
}