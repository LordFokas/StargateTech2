package lordfokas.stargatetech2.modules.automation;

import lordfokas.stargatetech2.api.bus.BusEvent;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutomationEventHandler {
	
	@SubscribeEvent
	public void remapAbstractBus(BusEvent evt){
		if(!evt.world.isRemote){
			if(evt instanceof BusEvent.RemoveFromNetwork){
				for(EnumFacing dir : EnumFacing.values()){
					RecursiveBusRemapper.scan(evt.world, evt.pos.offset(dir));
				}
			}else if(evt instanceof BusEvent.AddToNetwork){
				RecursiveBusRemapper.scan(evt.world, evt.pos);
			}
		}
	}
}
