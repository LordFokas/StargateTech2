package lordfokas.stargatetech2.modules.automation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import lordfokas.stargatetech2.api.bus.IBusDevice;
import lordfokas.stargatetech2.api.bus.IBusInterface;
import lordfokas.stargatetech2.modules.ModuleAutomation;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RecursiveBusRemapper {
	public static void scan(World w, BlockPos start){
		if(w.isRemote) return;
		scan(w, start, true);
	}
	
	private static void scan(World w, BlockPos start, boolean allowMachine){
		if(w.getBlockState(start).getBlock() instanceof BlockBusCable){
			scanCable(w, start);
		}else if(w.getTileEntity(start) instanceof IBusDevice && allowMachine){
			for(EnumFacing direction : EnumFacing.values()){
				scan(w, start.offset(direction), false);
			}
		}
	}
	
	private static void scanCable(World w, BlockPos start){
		Set<BlockPos> memory = new HashSet();
		Set<RemoteDevice> interfaces = new HashSet();
		propagateScan(w, start, memory, interfaces);
		ArrayList ifmemory = new ArrayList();
		for(RemoteDevice device : interfaces){
			Set<RemoteDevice> addressingTable = new HashSet();
			for(RemoteDevice address : interfaces){
				if(address != device){
					addressingTable.add(address);
				}
			}
			TileEntity te = w.getTileEntity(device.pos);
			if(te instanceof IBusDevice){
				for(IBusInterface b : ((IBusDevice)te).getInterfaces(device.side)){
					if(b instanceof BusInterface && !ifmemory.contains(b)){
						((BusInterface)b).setAddressingTable(device.side, addressingTable);
						ifmemory.add(b);
					}
				}
			}
		}
	}
	
	private static void propagateScan(World w, BlockPos location, Set<BlockPos> memory, Set<RemoteDevice> interfaces){
		if(!memory.contains(location)){
			memory.add(location);
			for(EnumFacing dir : EnumFacing.values()){
				ConnectionType connection = ModuleAutomation.busCable.getBusConnection(w, location, dir);
				if(connection.isConnected()){
					BlockPos next = location.offset(dir);
					if(connection.hasPlug()){
						interfaces.add(new RemoteDevice(next, dir.getOpposite()));
					}else{
						propagateScan(w, next, memory, interfaces);
					}
				}
			}
		}
	}
}