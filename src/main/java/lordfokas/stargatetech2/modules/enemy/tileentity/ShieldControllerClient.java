package lordfokas.stargatetech2.modules.enemy.tileentity;

import lordfokas.stargatetech2.lib.tileentity.ITile;
import lordfokas.stargatetech2.lib.tileentity.ITileContext;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.IFluidTank;

public class ShieldControllerClient extends ShieldControllerCommon implements ITileContext.Client{
	public boolean hasUpdated;
	
	@Override
	public void readNBTData(NBTTagCompound nbt) {
		super.readNBTData(nbt);
		hasUpdated = true;
	}
	
	@Override public void tick(){}
	@Override public boolean canTick(){ return false; }
	
	@Override
	public void setTile(ITile.Client tile){}
	
	public IFluidTank getTank(){
		return tank;
	}
}