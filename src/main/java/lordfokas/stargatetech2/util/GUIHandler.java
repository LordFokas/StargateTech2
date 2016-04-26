package lordfokas.stargatetech2.util;

import lordfokas.stargatetech2.lib.gui.BaseContainer;
import lordfokas.stargatetech2.modules.enemy.TileParticleIonizer;
import lordfokas.stargatetech2.modules.enemy.TileShieldController;
import lordfokas.stargatetech2.modules.enemy.gui.ContainerParticleIonizer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GUIHandler implements IGuiHandler {
	public enum Screen{
		SHIELD_CONTROLLER,
		PARTICLE_IONIZER,
	}
	
	@Override
	public BaseContainer getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
		BaseContainer container = null;
		switch(Screen.values()[ID]){
			case SHIELD_CONTROLLER:
				if(te instanceof TileShieldController)
					container = new BaseContainer((TileShieldController)te);
				break;
			case PARTICLE_IONIZER:
				/*if(te instanceof TileParticleIonizer) // FIXME: re-enable later
					container = new ContainerParticleIonizer((TileParticleIonizer)te, player);*/
				break;
			default: break;
		}
		return container;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
		return null;
	}
}