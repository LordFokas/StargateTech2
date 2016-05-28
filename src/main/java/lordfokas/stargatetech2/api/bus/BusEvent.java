package lordfokas.stargatetech2.api.bus;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

public class BusEvent extends Event{
	public final World world;
	public final BlockPos pos;
	
	protected BusEvent(World world, BlockPos pos){
		this.world = world;
		this.pos = pos;
	}
	
	/**
	 * Fire this event on Forge's BUS_EVENT to add the IBusDevice
	 * in this location to a bus network, if any is available.
	 * 
	 * @author LordFokas
	 */
	public static final class AddToNetwork extends BusEvent{
		public AddToNetwork(World world, BlockPos pos) {
			super(world, pos);
		}
	}
	
	/**
	 * Fire this event on Forge's BUS_EVENT to remove the IBusDevice
	 * in this location from any connected bus networks.
	 * 
	 * @author LordFokas
	 */
	public static final class RemoveFromNetwork extends BusEvent{
		public RemoveFromNetwork(World world, BlockPos pos) {
			super(world, pos);
		}
	}
}