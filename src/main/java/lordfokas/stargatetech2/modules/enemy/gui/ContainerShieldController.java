package lordfokas.stargatetech2.modules.enemy.gui;

import lordfokas.stargatetech2.lib.gui.BaseContainer;
import lordfokas.stargatetech2.modules.enemy.TileShieldController;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerShieldController extends BaseContainer{
	public TileShieldController controller;
	private int lastParticles = -1;
	
	public ContainerShieldController(TileShieldController tsc){
		this.controller = tsc;
	}
	
	@Override
	public void detectAndSendChanges(){
		super.detectAndSendChanges();
		int particles = controller.getIonAmount();
		if(particles != lastParticles){
			lastParticles = particles;
			sendUpdate(0, particles);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void updateProgressBar(int key, int value){
		if(key == 0){
			controller.setIonAmount(value);
		}
	}
}