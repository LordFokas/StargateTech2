package stargatetech2.common.base;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class BaseContainer extends Container {
	protected BaseTileEntity tileEntity;
	
	public BaseContainer(BaseTileEntity bte){
		tileEntity = bte;
	}
	
	public BaseTileEntity getTileEntity(){
		return tileEntity;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}
	
	public void forceClientUpdate(){
		tileEntity.updateClients();
	}
}
