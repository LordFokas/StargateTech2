package lordfokas.stargatetech2.util;

import lordfokas.stargatetech2.modules.enemy.TileParticleIonizer;
import lordfokas.stargatetech2.modules.enemy.TileShieldController;
import lordfokas.stargatetech2.modules.enemy.gui.ContainerShieldController;
import lordfokas.stargatetech2.modules.enemy.gui.GUIShieldController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GUIHandlerClient extends GUIHandler {
	
	@Override
	public GUIShieldController getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
		TileEntity te = world.getTileEntity(x, y, z);
		GUIShieldController gui = null;
		switch(Screen.values()[ID]){
			case SHIELD_CONTROLLER:
				if(te instanceof TileShieldController)
					gui = new GUIShieldController(new ContainerShieldController((TileShieldController)te));
				break;
			case PARTICLE_IONIZER:
				if(te instanceof TileParticleIonizer)
					//gui = new GUIParticleIonizer(new ContainerParticleIonizer((TileParticleIonizer)te, player));
				break;
			default: break;
		}
		return gui;
	}
}