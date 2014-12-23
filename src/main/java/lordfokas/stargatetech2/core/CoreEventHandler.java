package lordfokas.stargatetech2.core;

import lordfokas.stargatetech2.ModuleEnemy;
import lordfokas.stargatetech2.ModuleTransport;
import lordfokas.stargatetech2.api.bus.BusEvent;
import lordfokas.stargatetech2.automation.RecursiveBusRemapper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CoreEventHandler {
	@SubscribeEvent
	public void remapAbstractBus(BusEvent evt){
		if(!evt.world.isRemote){
			if(evt instanceof BusEvent.RemoveFromNetwork){
				Vec3Int position = new Vec3Int(evt.x, evt.y, evt.z);
				for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
					RecursiveBusRemapper.scan(evt.world, position.offset(dir));
				}
			}else if(evt instanceof BusEvent.AddToNetwork){
				RecursiveBusRemapper.scan(evt.world, new Vec3Int(evt.x, evt.y, evt.z));
			}
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void handleBlockHighlight(DrawBlockHighlightEvent evt){
		MovingObjectPosition mop = evt.target;
		Block b = Minecraft.getMinecraft().theWorld.getBlock(mop.blockX, mop.blockY, mop.blockZ);
		if(b == ModuleEnemy.shield || b == ModuleTransport.invisible){
			evt.setCanceled(true);
		}
	}
}