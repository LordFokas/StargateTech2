package lordfokas.stargatetech2.transport.util;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import lordfokas.stargatetech2.transport.packet.PacketActivateRings;
import lordfokas.stargatetech2.transport.tileentity.TileTransportRing;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;

public class RingKeyHandler extends KeyHandler {
	private static final KeyBinding RING_UP = new KeyBinding("[SGTech2] Activate rings (Up)", 200, "SGTech2");
	private static final KeyBinding RING_DOWN = new KeyBinding("[SGTech2] Activate rings (Down)", 208, "SGTech2");
	
	public RingKeyHandler(){
		super(new KeyBinding[]{RING_UP, RING_DOWN}, new boolean[]{true, true});
	}
	
	@Override
	public String getLabel(){
		return "StargateTech2:RingKeyHandler";
	}
	
	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat){}
	
	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd){
		if(!tickEnd) return;
		if(kb.getKeyCode() == RING_UP.getKeyCode()){
			makePlayerTriggerRings(true);
		}else if(kb.getKeyCode() == RING_DOWN.getKeyCode()){
			makePlayerTriggerRings(false);
		}
	}
	
	@Override
	public EnumSet<TickType> ticks(){
		return EnumSet.of(TickType.CLIENT);
	}
	
	private void makePlayerTriggerRings(boolean up){
		TileTransportRing rings = TileTransportRing.getRingsInRange(FMLClientHandler.instance().getClient().theWorld);
		if(rings != null && Minecraft.getMinecraft().currentScreen == null){
			PacketActivateRings packet = new PacketActivateRings();
			packet.x = rings.xCoord;
			packet.y = rings.yCoord;
			packet.z = rings.zCoord;
			packet.up = up;
			packet.sendToServer();
		}
	}
}