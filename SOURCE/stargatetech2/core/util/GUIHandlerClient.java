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
import stargatetech2.factory.gui.ContainerCrossover;
import stargatetech2.factory.gui.GUICrossover;
import stargatetech2.factory.tileentity.TileCrossover;

public class GUIHandlerClient extends GUIHandler {
	
	@Override
	public BaseGUI getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
		TileEntity te = world.getBlockTileEntity(x, y, z);
		BaseGUI gui = null;
		switch(Screen.values()[ID]){
			case SHIELD_CONTROLLER:
				if(te instanceof TileShieldEmitter)
					gui = new GUIShieldEmitter(new ContainerShieldEmitter((TileShieldEmitter)te));
				break;
			case PARTICLE_IONIZER:
				if(te instanceof TileParticleIonizer)
					gui = new GUIParticleIonizer(new ContainerParticleIonizer((TileParticleIonizer)te, player));
				break;
			case CROSSOVER:
				if(te instanceof TileCrossover)
					gui = new GUICrossover(new ContainerCrossover((TileCrossover)te, player));
			default: break;
		}
		return gui;
	}
}