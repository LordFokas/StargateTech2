package lordfokas.stargatetech2.lib.tileentity.component;

import lordfokas.stargatetech2.lib.tileentity.faces.IFaceColorFilter;
import lordfokas.stargatetech2.lib.tileentity.faces.IFacingAware;
import lordfokas.stargatetech2.lib.tileentity.faces.IFacingProvider;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class SidedComponent implements IAccessibleTileComponent, IFacingAware{
	private IFacingProvider facing;
	private IFaceColorFilter access, input, output;
	
	@Override
	public void setProvider(IFacingProvider provider) {
		this.facing = provider;
	}

	@Override
	public boolean accessibleOnSide(ForgeDirection side) {
		return matchFilter(side, access);
	}
	
	public boolean canInputOnSide(ForgeDirection side){
		return matchFilter(side, input);
	}
	
	public boolean canOutputOnSide(ForgeDirection side){
		return matchFilter(side, output);
	}
	
	private boolean matchFilter(ForgeDirection side, IFaceColorFilter filter){
		if(filter == null || side == ForgeDirection.UNKNOWN) return false;
		return filter.doesColorMatch(facing.getColorForDirection(side));
	}
	
	public SidedComponent setGenericAccessFilter(IFaceColorFilter filter){
		if(input != null || output != null) throw new RuntimeException("Can't set generic filter because component is already input/output");
		access = filter;
		return this;
	}
	
	public SidedComponent setInputFilter(IFaceColorFilter filter){
		if(access != null) throw new RuntimeException("Can't set input/output filter because component is already generic access");
		input = filter;
		return this;
	}
	
	public SidedComponent setOutputFilter(IFaceColorFilter filter){
		if(access != null) throw new RuntimeException("Can't set input/output filter because component is already generic access");
		output = filter;
		return this;
	}
}
