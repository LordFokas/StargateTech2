package stargatetech2.core.network.bus;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import stargatetech2.api.bus.IBusDevice;
import stargatetech2.api.bus.IBusInterface;
import stargatetech2.common.util.Vec3Int;
import stargatetech2.common.util.Vec4Int;
import stargatetech2.core.block.BlockBusCable;

public class RecursiveBusRemapper {
	
	public static void scan(World w, Vec3Int start){
		if(w.isRemote) return;
		scan(w, start, true);
	}
	
	private static void scan(World w, Vec3Int start, boolean allowMachine){
		if(Block.blocksList[w.getBlockId(start.x, start.y, start.z)] instanceof BlockBusCable){
			List<Vec3Int> memory = new ArrayList();
			scan(w, start, memory);
		}else if(w.getBlockTileEntity(start.x, start.y, start.z) instanceof IBusDevice && allowMachine){
			for(int i = 0; i < 6; i++){
				IBusInterface[] interfaces = ((IBusDevice)w.getBlockTileEntity(start.x, start.y, start.z)).getInterfaces(i);
				for(IBusInterface iface : interfaces){
					if(iface instanceof BusInterface){
						((BusInterface)iface).clearAddressingTable();
					}
				}
			}
			for(ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS){
				scan(w, start.offset(direction), false);
			}
		}
	}
	
	private static void scan(World w, Vec3Int start, List<Vec3Int> memory){
		if(!memory.contains(start)){
			
		}
	}
}