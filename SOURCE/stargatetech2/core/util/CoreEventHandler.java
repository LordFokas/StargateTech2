package stargatetech2.core.util;

import stargatetech2.core.ModuleCore;
import stargatetech2.core.block.BlockShield;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.event.ForgeSubscribe;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CoreEventHandler {	
	@ForgeSubscribe
	@SideOnly(Side.CLIENT)
	public void handleBlockHighlight(DrawBlockHighlightEvent event){
		MovingObjectPosition mop = event.target;
		World world = event.context.theWorld;
		if(world.getBlockId(mop.blockX, mop.blockY, mop.blockZ) == ModuleCore.shield.blockID){
			event.setCanceled(true);
		}
	}
}
