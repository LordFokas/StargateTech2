package stargatetech2.core.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import stargatetech2.core.base.BaseGUI;
import stargatetech2.enemy.gui.ContainerParticleIonizer;
import stargatetech2.enemy.gui.ContainerShieldEmitter;
import stargatetech2.enemy.gui.GUIParticleIonizer;
import stargatetech2.enemy.gui.GUIShieldEmitter;
import stargatetech2.enemy.tileentity.TileParticleIonizer;
import stargatetech2.enemy.tileentity.TileShieldEmitter;

public class GUIHandlerClient extends GUIHandler {
	
	@Override
	public BaseGUI getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
		TileEntity te = world.getBlockTileEntity(x, y, z);
		BaseGUI gui = null;
		switch(Screen.values()[ID]){
			case SHIELD_EMITTER:
				if(te instanceof TileShieldEmitter)
					gui = new GUIShieldEmitter(new ContainerShieldEmitter((TileShieldEmitter)te));
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