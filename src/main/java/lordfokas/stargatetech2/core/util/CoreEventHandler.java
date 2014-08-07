package lordfokas.stargatetech2.core.util;

import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.event.ForgeSubscribe;
import lordfokas.stargatetech2.api.bus.BusEvent;
import lordfokas.stargatetech2.automation.bus.RecursiveBusRemapper;
import lordfokas.stargatetech2.enemy.ModuleEnemy;
import lordfokas.stargatetech2.transport.ModuleTransport;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CoreEventHandler {
	@ForgeSubscribe
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
	
	@ForgeSubscribe
	@SideOnly(Side.CLIENT)
	public void handleBlockHighlight(DrawBlockHighlightEvent evt){
		MovingObjectPosition mop = evt.target;
		World world = evt.context.theWorld;
		int blockID = world.getBlockId(mop.blockX, mop.blockY, mop.blockZ);
		if(blockID == ModuleEnemy.shield.blockID || blockID == ModuleTransport.invisible.blockID){
			evt.setCanceled(true);
		}
	}
}