package stargatetech2.common.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import stargatetech2.common.base.BaseContainer;
import stargatetech2.core.gui.ContainerNaquadahCapacitor;
import stargatetech2.core.gui.ContainerParticleIonizer;
import stargatetech2.core.gui.ContainerShieldEmitter;
import stargatetech2.core.tileentity.TileNaquadahCapacitor;
import stargatetech2.core.tileentity.TileParticleIonizer;
import stargatetech2.core.tileentity.TileShieldEmitter;
import cpw.mods.fml.common.network.IGuiHandler;

public class GUIHandler implements IGuiHandler {
	public enum Screen{
		SHIELD_EMITTER,
		PARTICLE_IONIZER,
		NAQUADAH_CAPACITOR
	}
	
	@Override
	public BaseContainer getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity te = world.getBlockTileEntity(x, y, z);
		BaseContainer container = null;
		switch(Screen.values()[ID]){
			case SHIELD_EMITTER:
				if(te instanceof TileShieldEmitter)
					container = new ContainerShieldEmitter((TileShieldEmitter)te);
				break;
			case PARTICLE_IONIZER:
				if(te instanceof TileParticleIonizer)
					container = new ContainerParticleIonizer((TileParticleIonizer)te, player);
				break;
			case NAQUADAH_CAPACITOR:
				if(te instanceof TileNaquadahCapacitor)
					container = new ContainerNaquadahCapacitor((TileNaquadahCapacitor)te);
			default: break;
		}
		return container;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
		return null;
	}
}