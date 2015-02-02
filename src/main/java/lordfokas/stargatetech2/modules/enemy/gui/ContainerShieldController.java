package lordfokas.stargatetech2.modules.enemy.gui;

import lordfokas.stargatetech2.lib.gui.BaseContainer;
import lordfokas.stargatetech2.modules.enemy.TileShieldController;

public class ContainerShieldController extends BaseContainer{
	public TileShieldController controller;
	private int lastParticles = -1;
	
	public ContainerShieldController(TileShieldController tsc){
		this.controller = tsc;
	}
	
	
}