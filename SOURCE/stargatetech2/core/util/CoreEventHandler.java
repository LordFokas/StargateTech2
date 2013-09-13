package stargatetech2.core.util;

import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.event.ForgeSubscribe;
import stargatetech2.core.ModuleCore;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CoreEventHandler {	
	@ForgeSubscribe
	@SideOnly(Side.CLIENT)
	public void handleBlockHighlight(DrawBlockHighlightEvent event){
		MovingObjectPosition mop = event.target;
		World world = event.context.theWorld;
		int blockID = world.getBlockId(mop.blockX, mop.blockY, mop.blockZ);
		if(blockID == ModuleCore.shield.blockID || blockID == ModuleCore.invisible.blockID){
			event.setCanceled(true);
		}
	}
}