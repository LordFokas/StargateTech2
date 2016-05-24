package lordfokas.stargatetech2.modules.core;

import lordfokas.stargatetech2.api.bus.BusEvent;
import lordfokas.stargatetech2.modules.ModuleEnemy;
import lordfokas.stargatetech2.modules.ModuleTransport;
import lordfokas.stargatetech2.modules.automation.RecursiveBusRemapper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CoreEventHandler {
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
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void handleBlockHighlight(DrawBlockHighlightEvent evt){
		MovingObjectPosition mop = evt.target;
		Block b = Minecraft.getMinecraft().theWorld.getBlockState(mop.getBlockPos()).getBlock();
		if(b == ModuleEnemy.shield || b == ModuleTransport.invisible){
			evt.setCanceled(true);
		}
	}
}