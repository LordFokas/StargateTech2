package lordfokas.stargatetech2.modules.core;

import lordfokas.stargatetech2.modules.ModuleEnemy;
import lordfokas.stargatetech2.modules.ModuleTransport;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CoreEventHandler {
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void handleBlockHighlight(DrawBlockHighlightEvent evt){
		RayTraceResult rtr = evt.getTarget();
		Block b = Minecraft.getMinecraft().theWorld.getBlockState(rtr.getBlockPos()).getBlock();
		if(b == ModuleEnemy.shield || b == ModuleTransport.invisible){
			evt.setCanceled(true);
		}
	}
}