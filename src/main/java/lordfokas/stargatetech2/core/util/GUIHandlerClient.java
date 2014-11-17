package lordfokas.stargatetech2.core.util;

import lordfokas.stargatetech2.core.base.BaseGUI;
import lordfokas.stargatetech2.enemy.gui.ContainerParticleIonizer;
import lordfokas.stargatetech2.enemy.gui.ContainerShieldController;
import lordfokas.stargatetech2.enemy.gui.GUIParticleIonizer;
import lordfokas.stargatetech2.enemy.gui.GUIShieldController;
import lordfokas.stargatetech2.enemy.tileentity.TileParticleIonizer;
import lordfokas.stargatetech2.enemy.tileentity.TileShieldController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GUIHandlerClient extends GUIHandler {
	
	@Override
	public BaseGUI getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
		TileEntity te = world.getTileEntity(x, y, z);
		BaseGUI gui = null;
		switch(Screen.values()[ID]){
			case SHIELD_CONTROLLER:
				if(te instanceof TileShieldController)
					gui = new GUIShieldController(new ContainerShieldController((TileShieldController)te));
				break;
			case PARTICLE_IONIZER:
				if(te instanceof TileParticleIonizer)
					gui = new GUIParticleIonizer(new ContainerParticleIonizer((TileParticleIonizer)te, player));
				break;
			default: break;
		}
		return gui;
	}
}