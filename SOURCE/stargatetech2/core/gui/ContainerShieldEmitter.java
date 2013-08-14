package stargatetech2.core.gui;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import stargatetech2.common.base.BaseContainer;
import stargatetech2.common.base.BaseTileEntity;
import stargatetech2.core.tileentity.TileShieldEmitter;

public class ContainerShieldEmitter extends BaseContainer {
	int lastIons = 0;
	
	public ContainerShieldEmitter(TileShieldEmitter tse){ super(tse); }
	
	@Override
	public void detectAndSendChanges(){
		super.detectAndSendChanges();
		int ions = ((TileShieldEmitter)tileEntity).getIonAmount();
		if(lastIons != ions){
			System.out.println("Is Sending Update!");
			for(ICrafting c : (List<ICrafting>)crafters){
				c.sendProgressBarUpdate(this, 0, ions);
			}
			lastIons = ions;
		}
	}
	
	@Override
	public void updateProgressBar(int v, int d){
		if(v == 0){
			((TileShieldEmitter)tileEntity).setIonAmount(d);
			System.out.println("Is Updating!");
		}
	}
	
	@Override
	public void addCraftingToCrafters(ICrafting crafting){
		super.addCraftingToCrafters(crafting);
		crafting.sendProgressBarUpdate(this, 0, ((TileShieldEmitter)tileEntity).getIonAmount());
	}
}