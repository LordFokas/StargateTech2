package stargatetech2.factory.gui;

import net.minecraft.entity.player.EntityPlayer;
import stargatetech2.core.base.BaseContainer;
import stargatetech2.factory.tileentity.TilePrioritizer;

public class ContainerPrioritizer extends BaseContainer {
	public final TilePrioritizer prioritizer;
	
	public ContainerPrioritizer(TilePrioritizer prioritizer, EntityPlayer player){
		this.prioritizer = prioritizer;
	}
}
