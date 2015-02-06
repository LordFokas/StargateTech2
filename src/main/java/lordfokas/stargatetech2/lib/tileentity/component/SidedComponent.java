package lordfokas.stargatetech2.lib.tileentity.component;

import lordfokas.stargatetech2.lib.tileentity.faces.FaceColor;
import lordfokas.stargatetech2.lib.tileentity.faces.IFacingAware;
import lordfokas.stargatetech2.lib.tileentity.faces.IFacingProvider;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class SidedComponent implements IAccessibleTileComponent, IFacingAware{
	private IFacingProvider provider;
	
	@Override
	public void setProvider(IFacingProvider provider) {
		this.provider = provider;
	}

	@Override
	public boolean accessibleOnSide(ForgeDirection side) {
		FaceColor color = provider.getColorForDirection(side);
		return false;
	}
}
