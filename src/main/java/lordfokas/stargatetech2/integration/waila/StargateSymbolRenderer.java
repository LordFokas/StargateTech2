package lordfokas.stargatetech2.integration.waila;

import java.awt.Dimension;

import lordfokas.stargatetech2.api.stargate.Address;
import lordfokas.stargatetech2.core.Color;
import lordfokas.stargatetech2.core.reference.TextureReference;
import lordfokas.stargatetech2.transport.stargates.LoreAddresses;
import lordfokas.stargatetech2.transport.stargates.SymbolCoordinates;
import mcp.mobius.waila.api.IWailaCommonAccessor;
import mcp.mobius.waila.api.IWailaTooltipRenderer;
import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;

public class StargateSymbolRenderer implements IWailaTooltipRenderer{
	
	@Override
	public Dimension getSize(String[] params, IWailaCommonAccessor accessor) {
		return new Dimension(144, 18);
	}

	@Override
	public void draw(String[] params, IWailaCommonAccessor accessor, int x, int y) {
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureReference.SYMBOLS);
		Color c = Color.ORANGE;
		Address addr = LoreAddresses.EARTH;
		GL11.glPushMatrix();
		GL11.glColor3f(c.r(), c.g(), c.b());
		GL11.glTranslatef(x, y, 0);
		for(int i = 0; i < addr.length(); i++){
			SymbolCoordinates s = SymbolCoordinates.get(addr.getSymbol(i).ordinal());
			float px = i*16 + ((i/3)*8);
			GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
			GL11.glTexCoord2f(s.x0, s.y0);
			GL11.glVertex2f(px, 0);
			GL11.glTexCoord2f(s.x0, s.y1);
			GL11.glVertex2f(px, 16);
			GL11.glTexCoord2f(s.x1, s.y0);
			GL11.glVertex2f(px + 16, 0);
			GL11.glTexCoord2f(s.x1, s.y1);
			GL11.glVertex2f(px + 16, 16);
			GL11.glEnd();
		}
		GL11.glPopMatrix();
	}
}
