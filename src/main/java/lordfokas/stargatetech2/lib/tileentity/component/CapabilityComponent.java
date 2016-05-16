package lordfokas.stargatetech2.lib.tileentity.component;

import lordfokas.stargatetech2.lib.tileentity.faces.FaceColor;
import lordfokas.stargatetech2.lib.tileentity.faces.IFacingAware;
import lordfokas.stargatetech2.lib.tileentity.faces.IFacingProvider;
import net.minecraft.util.EnumFacing;

public abstract class CapabilityComponent<C> implements ICapabilityTileComponent<C>, IFacingAware{
	private IFacingProvider facing;
	
	@Override
	public final void setProvider(IFacingProvider provider) {
		this.facing = provider;
	}
	
	protected final FaceColor getColor(EnumFacing color){
		return facing.getColorForSide(color);
	}
	
	@Override
	public boolean discardIfNull(){
		return true;
	}
}
