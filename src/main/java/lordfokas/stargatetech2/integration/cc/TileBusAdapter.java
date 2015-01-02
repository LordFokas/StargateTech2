package lordfokas.stargatetech2.integration.cc;

import java.util.LinkedList;

import lordfokas.stargatetech2.api.StargateTechAPI;
import lordfokas.stargatetech2.api.bus.BusPacket;
import lordfokas.stargatetech2.api.bus.BusPacketLIP;
import lordfokas.stargatetech2.api.bus.BusPacketLIP.LIPMetadata;
import lordfokas.stargatetech2.api.bus.BusPacketNetScan;
import lordfokas.stargatetech2.api.bus.BusPacketNetScan.Device;
import lordfokas.stargatetech2.api.bus.IBusDevice;
import lordfokas.stargatetech2.api.bus.IBusInterface;
import lordfokas.stargatetech2.automation.AddressHelper;
import lordfokas.stargatetech2.core.base.BaseTileEntity;
import lordfokas.stargatetech2.core.reference.ModReference;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;

public class TileBusAdapter extends BaseTileEntity implements IBusDevice, IPeripheral{
	private LinkedList<BusPacket> outputQueue = new LinkedList();
	private LinkedList<BusPacketLIP> received = new LinkedList();
	private CCBusDriver networkDriver = new CCBusDriver(this);
	private IBusInterface[] interfaces = new IBusInterface[]{
			StargateTechAPI.api().getFactory().getIBusInterface(this, networkDriver)
	};
	private int attachedComputers = 0;
	private IComputerAccess computer;
	private BusPacketLIP packet;
	
	@Override
	public boolean canUpdate(){
		return false;
	}
	
	//############################### CC STUFF
	private static enum ComputerMethod{
		SCANNETWORK	("scanNetwork"),
		
		SETADDRESS	("setAddress"),
		GETADDRESS	("getAddress"),
		SENDPACKET	("sendPacket"),
		GETRECVCOUNT("getRecvCount"),
		
		PULLPACKET("pullPacket"),
		DISPOSEPACKET("disposePacket"),
		GETFIELDLIST("getFieldList"),
		GETFIELD("getField"),
		LISTMETHODS("listMethods");
		
		private String name;
		public static final String[] ALL;
		
		private ComputerMethod(String name){
			this.name = name;
		}
		
		static{
			ALL = new String[values().length];
			for(ComputerMethod m : values()){
				ALL[m.ordinal()] = m.name;
			}
		}
	}
	
	@Override
	public String getType() {
		return "Abstract Bus Adapter";
	}

	@Override
	public String[] getMethodNames() {
		return ComputerMethod.ALL;
	}

	@Override
	public synchronized Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException {
		ComputerMethod m = ComputerMethod.values()[method];
		try{
		switch(m){
			case SCANNETWORK:
				if(arguments.length > 2) throw new LuaException("Too many args! USAGE: scanNetwork([options [, address]])");
				boolean status, location, name, desc, addr;
				short address = (short)0xFFFF;
				if(arguments.length == 2) try{
					address = AddressHelper.convert((String)arguments[1]);
				}catch(Exception ignored){}
				if(arguments.length > 0 && arguments[0] instanceof String){
					String options = ((String) arguments[0]).toLowerCase();
					location = options.contains("l");
					status = options.contains("e");
					name = options.contains("n");
					desc = options.contains("d");
					addr = options.contains("a");
				}else{
					name = addr = status = true;
					location = desc = false;
				}
				BusPacketNetScan scan = new BusPacketNetScan(address);
				outputQueue.add(scan);
				interfaces[0].sendAllPackets();
				LinkedList<String> deviceList = new LinkedList();
				for(Device device : scan.getDevices()){
					StringBuffer str = new StringBuffer();
					if(status) str.append("[").append(device.enabled ? "E" : "D").append("] ");
					if(addr) str.append("0x").append(AddressHelper.convert(device.address)).append(" ");
					if(name) str.append(device.name).append(" ");
					if(location) str.append("{").append(device.x).append(", ").append(device.y).append(", ").append(device.z).append("} ");
					if(desc) str.append("\n").append(device.description).append("\n");
					deviceList.add(str.toString());
				}
				return deviceList.toArray();
				
			case GETADDRESS: // GETS THE CURRENT ADDRESS FROM THE NETWORK DRIVER
				return new Object[]{AddressHelper.convert(networkDriver.getInterfaceAddress())};
				
			case GETRECVCOUNT: // GETS THE NUMBER OF PACKETS IN THE RECEIVE QUEUE
				return new Object[]{received.size()};
				
			case SENDPACKET: // SENDS A PACKET TO THE REST OF THE NETWORK
				if(arguments.length < 2) throw new LuaException("Not enough arguments (min. 2 args)");
				BusPacketLIP output = new BusPacketLIP(networkDriver.getInterfaceAddress(), AddressHelper.convert((String)arguments[0]));
				output.setMetadata(new LIPMetadata(ModReference.MOD_ID, "BusAdapter", ""));
				for(int i = 1; i < arguments.length; i++){
					String arg = (String) arguments[i];
					int split = arg.indexOf(": ");
					if(split < 0) throw new LuaException("Bad argument #" + i);
					output.set(arg.substring(0, split), arg.substring(split+2));
				}
				output.finish();
				outputQueue.add(output);
				interfaces[0].sendAllPackets();
				return output.getResponses().toArray();
				
			case SETADDRESS: // SETS THE SPECIFIED ADDRESS ON THE NETWORK DRIVER
				if(arguments.length == 1 && arguments[0] instanceof String){
					networkDriver.setInterfaceAddress(AddressHelper.convert((String)arguments[0]));
					return new Object[]{true};
				}
				return new Object[]{false};
				
			// ### PACKET HANDLING ##################################################################
			case PULLPACKET:
				boolean hadPacket = !received.isEmpty();
				if(hadPacket) packet = received.removeFirst();
				return new Object[]{hadPacket};
			case DISPOSEPACKET:
				boolean changed = (packet != null);
				packet = null;
				return new Object[]{changed};
			case GETFIELDLIST:
				return packet.getEntryList().toArray();
			case GETFIELD:
				return new Object[]{packet.get((String)arguments[0])};
			case LISTMETHODS:
				return getMethodNames();
			default: break;
		}
		}catch(Exception e){
			throw new LuaException(e.getMessage());
		}
		return null;
	}

	public boolean canAttachToSide(int side) {
		return attachedComputers == 0;
	}

	@Override
	public synchronized void attach(IComputerAccess computer){
		networkDriver.setEnabled(true);
		this.computer = computer;
		attachedComputers = 1;
	}

	@Override
	public synchronized void detach(IComputerAccess computer){
		networkDriver.setEnabled(false);
		attachedComputers = 0;
		this.computer = null;
	}
	
	//############################# BUS STUFF
	@Override
	public IBusInterface[] getInterfaces(int side){
		return interfaces;
	}
	
	public void handlePacket(BusPacketLIP packet){
		if(computer != null){
			received.add(packet);
			computer.queueEvent("busPacketReceived", null);
		}
	}
	
	public BusPacket getNextPacket(){
		if(outputQueue.isEmpty()) return null;
		else return outputQueue.removeFirst();
	}
	
	@Override
	public World getWorld() {
		return worldObj;
	}
	
	@Override
	public int getXCoord() {
		return xCoord;
	}

	@Override
	public int getYCoord() {
		return yCoord;
	}

	@Override
	public int getZCoord() {
		return zCoord;
	}
	
	//############################# NBT STUFF
	@Override
	protected void readNBT(NBTTagCompound nbt){
		interfaces[0].readFromNBT(nbt, "interface");
		networkDriver.setInterfaceAddress(nbt.getShort("address"));
	}
	
	@Override
	protected void writeNBT(NBTTagCompound nbt){
		interfaces[0].writeToNBT(nbt, "interface");
		nbt.setShort("address", networkDriver.getInterfaceAddress());
	}

	@Override
	public boolean equals(IPeripheral other) {
		
		return other==this;
	}
}
