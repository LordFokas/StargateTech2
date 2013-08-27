package stargatetech2.core.gui;

import stargatetech2.common.base.BaseContainer;
import stargatetech2.core.tileentity.TileShieldEmitter;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerShieldEmitter extends BaseContainer{
	public TileShieldEmitter emitter;
	private int lastParticles = -1;
	
	public ContainerShieldEmitter(TileShieldEmitter tse){
		this.emitter = tse;
	}
	
	@Override
	public void detectAndSendChanges(){
		super.detectAndSendChanges();
		int particles = emitter.getIonAmount();
		if(particles != lastParticles){
			lastParticles = particles;
			sendUpdate(0, particles);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void updateProgressBar(int key, int value){
		if(key == 0){
			emitter.setIonAmount(value);
		}
	}
}