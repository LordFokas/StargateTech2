package lordfokas.stargatetech2.modules.integration.waila;

import java.awt.Dimension;

import lordfokas.stargatetech2.api.stargate.Address;
import lordfokas.stargatetech2.api.stargate.ITileStargate;
import lordfokas.stargatetech2.lib.render.Color;
import lordfokas.stargatetech2.modules.transport.stargates.SymbolCoordinates;
import lordfokas.stargatetech2.reference.TextureReference;
import mcp.mobius.waila.api.IWailaCommonAccessor;
import mcp.mobius.waila.api.IWailaTooltipRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;

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
		TileEntity te = accessor.getTileEntity();
		Address addr = Address.ERROR;
		if(te instanceof ITileStargate){
			addr = ((ITileStargate)te).getAddress();
		}
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
