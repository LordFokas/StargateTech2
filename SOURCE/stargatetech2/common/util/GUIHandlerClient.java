package stargatetech2.common.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import stargatetech2.common.base.BaseGUI;
import stargatetech2.common.util.GUIHandler.Screen;
import stargatetech2.core.gui.ContainerNaquadahCapacitor;
import stargatetech2.core.gui.ContainerParticleIonizer;
import stargatetech2.core.gui.ContainerShieldEmitter;
import stargatetech2.core.gui.GUINaquadahCapacitor;
import stargatetech2.core.gui.GUIParticleIonizer;
import stargatetech2.core.gui.GUIShieldEmitter;
import stargatetech2.core.tileentity.TileNaquadahCapacitor;
import stargatetech2.core.tileentity.TileParticleIonizer;
import stargatetech2.core.tileentity.TileShieldEmitter;

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
			case NAQUADAH_CAPACITOR:
				if(te instanceof TileNaquadahCapacitor)
					gui = new GUINaquadahCapacitor(new ContainerNaquadahCapacitor((TileNaquadahCapacitor)te));
			default: break;
		}
		return gui;
	}
}