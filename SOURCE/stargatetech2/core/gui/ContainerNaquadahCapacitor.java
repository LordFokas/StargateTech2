package stargatetech2.core.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import stargatetech2.common.base.BaseContainer;
import stargatetech2.core.tileentity.TileNaquadahCapacitor;

public class ContainerNaquadahCapacitor extends BaseContainer {
	public TileNaquadahCapacitor nc;
	int prevPower = -1;
	
	public ContainerNaquadahCapacitor(TileNaquadahCapacitor nc){
		this.nc = nc;
	}
	
	@Override
	public void detectAndSendChanges(){
		int power = nc.getPower();
		if(power != prevPower){
			sendUpdate(0, power);
			prevPower = power;
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void updateProgressBar(int key, int value){
		if(key == 0){
			nc.setPower(value);
		}
	}
}