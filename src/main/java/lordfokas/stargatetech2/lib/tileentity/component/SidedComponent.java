package lordfokas.stargatetech2.lib.tileentity.component;

import lordfokas.stargatetech2.lib.tileentity.FaceColor;
import lordfokas.stargatetech2.lib.tileentity.IFacingAware;
import lordfokas.stargatetech2.lib.tileentity.IFacingProvider;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class SidedComponent implements IAccessibleTileComponent, IFacingAware{
	
	private IFacingProvider provider;
	
	@Override
	public void setProvider(IFacingProvider provider) {
		this.provider = provider;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean accessibleOnSide(ForgeDirection side) {
		FaceColor color = provider.getColorForDirection(side);
		return false;
	}
}
