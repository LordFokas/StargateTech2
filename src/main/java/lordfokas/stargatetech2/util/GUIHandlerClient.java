package lordfokas.stargatetech2.util;

import lordfokas.naquadria.gui.BaseContainer;
import lordfokas.stargatetech2.modules.enemy.TileShieldController;
import lordfokas.stargatetech2.modules.enemy.gui.GUIShieldController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GUIHandlerClient extends GUIHandler {
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
		TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
		Object gui = null;
		switch(Screen.values()[ID]){
			case SHIELD_CONTROLLER:
				if(te instanceof TileShieldController)
					gui = new GUIShieldController(new BaseContainer((TileShieldController)te));
				break;
			case PARTICLE_IONIZER:
				/*if(te instanceof TileParticleIonizer) // FIXME re-enable when fixed
					gui = new GUIParticleIonizer(new ContainerParticleIonizer((TileParticleIonizer)te, player));*/
				break;
			default: break;
		}
		return gui;
	}
}