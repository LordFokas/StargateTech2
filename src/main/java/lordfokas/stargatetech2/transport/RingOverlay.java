package lordfokas.stargatetech2.transport;

import lordfokas.stargatetech2.core.ConfigClient;
import lordfokas.stargatetech2.core.reference.TextureReference;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RingOverlay extends Gui{
	private static final RingOverlay INSTANCE = new RingOverlay();
	
	private RingOverlay(){}
	
	public static void register(){
		MinecraftForge.EVENT_BUS.register(INSTANCE);
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void drawOverlay(RenderGameOverlayEvent.Post evt){
		if(!ConfigClient.enableRingGUIOverlay || evt.type != ElementType.ALL) return;
		Minecraft mc = FMLClientHandler.instance().getClient();
		TileTransportRing rings = TileTransportRing.getRingsInRange(mc.theWorld);
		if(rings == null) return;
		
		mc.renderEngine.bindTexture(TextureReference.GUI_RING_OVERLAY);
		int f = evt.resolution.getScaleFactor();
		int w = 32*f, h = 24*f;
		int px = 2*f, py = ((mc.displayHeight / f) - h) / 2;
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glTexCoord3f(0, 0, zLevel);
		GL11.glVertex2f(px, py);
		GL11.glTexCoord3f(0, 1, zLevel);
		GL11.glVertex2f(px, py + h);
		GL11.glTexCoord3f(1, 0, zLevel);
		GL11.glVertex2f(px + w, py);
		GL11.glTexCoord3f(1, 1, zLevel);
		GL11.glVertex2f(px + w, py + h);
		GL11.glEnd();
		
		// Hacks and shit. This clears the texture I bound.
		this.drawString(mc.fontRenderer, "", 0, 0, 0xffffff);
	}
}
