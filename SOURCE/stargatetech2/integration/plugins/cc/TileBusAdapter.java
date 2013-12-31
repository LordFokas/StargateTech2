package stargatetech2.integration.plugins.cc;

import java.util.LinkedList;

import net.minecraft.nbt.NBTTagCompound;
import stargatetech2.api.StargateTechAPI;
import stargatetech2.api.bus.BusPacket;
import stargatetech2.api.bus.BusPacketLIP;
import stargatetech2.api.bus.IBusDevice;
import stargatetech2.api.bus.IBusInterface;
import stargatetech2.common.base.BaseTileEntity;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.ILuaContext;
import dan200.computer.api.IPeripheral;

public class TileBusAdapter extends BaseTileEntity implements IBusDevice, IPeripheral{
	private LinkedList<BusPacket> outputQueue = new LinkedList();
	private LinkedList<BusPacketLIP> received = new LinkedList();
	private CCBusDriver networkDriver = new CCBusDriver(this);
	private IBusInterface[] interfaces = new IBusInterface[]{
			StargateTechAPI.api().getFactory().getIBusInterface(this, networkDriver)
	};
	private int attachedComputers = 0;
	private IComputerAccess computer;
	
	//############################### CC STUFF
	private static enum ComputerMethod{
		SETADDRESS	("setAddress"),
		GETADDRESS	("getAddress"),
		SENDPACKET	("sendPacket"),
		RECVPACKET	("recvPacket"),
		GETRECVCOUNT("getRecvCount");
		
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
	public synchronized Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws Exception {
		ComputerMethod m = ComputerMethod.values()[method];
		switch(m){
			case GETADDRESS: // GETS THE CURRENT ADDRESS FROM THE NETWORK DRIVER
				short address = networkDriver.getInterfaceAddress();
				return new Object[]{Integer.toHexString(address).substring(4)};
				
			case GETRECVCOUNT: // GETS THE NUMBER OF PACKETS IN THE RECEIVE QUEUE
				return new Object[]{received.size()};
				
			case RECVPACKET: // GETS THE NEXT PACKET IN THE RECEIVE QUEUE
				break;
				
			case SENDPACKET: // SENDS A PACKET TO THE REST OF THE NETWORK
				break;
				
			case SETADDRESS: // SETS THE SPECIFIED ADDRESS ON THE NETWORK DRIVER
				if(arguments.length == 1 && arguments[0] instanceof String){
					networkDriver.setAddress(Short.parseShort((String)arguments[0], 16));
					return new Object[]{true};
				}
				return new Object[]{false};
			default: break;
		}
		return null;
	}

	@Override
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
	
	//############################# NBT STUFF
	@Override
	protected void readNBT(NBTTagCompound nbt){}
	
	@Override
	protected void writeNBT(NBTTagCompound nbt){}
}
