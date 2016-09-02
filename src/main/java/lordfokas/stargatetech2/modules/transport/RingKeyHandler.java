package lordfokas.stargatetech2.modules.transport;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

public class RingKeyHandler{
	private static final RingKeyHandler INSTANCE = new RingKeyHandler();
	private static final KeyBinding RING_UP = new KeyBinding("[SGTech2] Activate rings (Up)", 200, "SGTech2");
	private static final KeyBinding RING_DOWN = new KeyBinding("[SGTech2] Activate rings (Down)", 208, "SGTech2");
	
	private RingKeyHandler(){}
	
	public static String up(){
		return RING_UP.getKeyCode() + "";
	}
	
	public static String down(){
		return RING_DOWN.getKeyCode() + "";
	}
	
	public static void register(){
		ClientRegistry.registerKeyBinding(RING_DOWN);
		ClientRegistry.registerKeyBinding(RING_UP);
		FMLCommonHandler.instance().bus().register(INSTANCE);
	}
	
	@SubscribeEvent
	public void keyUp(KeyInputEvent evt){
		if (!FMLClientHandler.instance().isGUIOpen(GuiScreen.class)){
			if(RING_UP.isPressed()){
				makePlayerTriggerRings(true);
			}else if(RING_DOWN.isPressed()){
				makePlayerTriggerRings(false);
			}
		}
	}
	
	private void makePlayerTriggerRings(boolean up){
		TileTransportRing rings = TileTransportRing.getRingsInRange(FMLClientHandler.instance().getClient().theWorld);
		if(rings != null && Minecraft.getMinecraft().currentScreen == null){
			PacketActivateRings packet = new PacketActivateRings();
			packet.coordinates = new BlockPos(rings.getXCoord(), rings.getYCoord(), rings.getZCoord());
			packet.up = up;
			packet.sendToServer();
		}
	}
}