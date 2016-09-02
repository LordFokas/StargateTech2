package lordfokas.stargatetech2.modules.automation;

import java.util.ArrayList;
import java.util.List;

import lordfokas.stargatetech2.api.bus.IBusDevice;
import lordfokas.stargatetech2.api.bus.IBusInterface;
import lordfokas.stargatetech2.modules.ModuleAutomation;
import lordfokas.stargatetech2.util.Vec4Int;
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
		ArrayList<BlockPos> memory = new ArrayList();
		ArrayList<Vec4Int> interfaces = new ArrayList();
		propagateScan(w, start, memory, interfaces);
		ArrayList ifmemory = new ArrayList();
		for(Vec4Int device : interfaces){
			ArrayList<Vec4Int> addressingTable = new ArrayList();
			for(Vec4Int address : interfaces){
				if(address != device){
					addressingTable.add(address);
				}
			}
			int s = device.w;
			int x = device.x;
			int y = device.y;
			int z = device.z;
			TileEntity te = w.getTileEntity(x, y, z);
			if(te instanceof IBusDevice){
				for(IBusInterface b : ((IBusDevice)te).getInterfaces(s)){
					if(b instanceof BusInterface && !ifmemory.contains(b)){
						((BusInterface)b).setAddressingTable(s, addressingTable);
						ifmemory.add(b);
					}
				}
			}
		}
	}
	
	private static void propagateScan(World w, BlockPos location, List<BlockPos> memory, List<Vec4Int> interfaces){
		if(!memory.contains(location)){
			memory.add(location);
			for(EnumFacing dir : EnumFacing.values()){
				Connection connection = ModuleAutomation.busCable.getBusConnection(w, location, dir);
				if(connection.isConnected()){
					BlockPos next = location.offset(dir);
					if(connection.hasPlug()){
						interfaces.add(new Vec4Int(dir.getOpposite().ordinal(), next.x, next.y, next.z));
					}else{
						propagateScan(w, next, memory, interfaces);
					}
				}
			}
		}
	}
}