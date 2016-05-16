package lordfokas.naquadria.tileentity.component;

import lordfokas.naquadria.tileentity.facing.FaceColor;
import lordfokas.naquadria.tileentity.facing.IFacingAware;
import lordfokas.naquadria.tileentity.facing.IFacingProvider;
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
