package stargatetech2.core.gui;

import stargatetech2.common.base.BaseContainer;
import stargatetech2.core.tileentity.TileShieldEmitter;

public class ContainerShieldEmitter extends BaseContainer{
	public TileShieldEmitter emitter;
	
	public ContainerShieldEmitter(TileShieldEmitter tse){
		this.emitter = tse;
	}
}