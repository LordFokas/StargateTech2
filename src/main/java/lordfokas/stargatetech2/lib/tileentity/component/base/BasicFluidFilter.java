package lordfokas.stargatetech2.lib.tileentity.component.base;

import lordfokas.stargatetech2.lib.tileentity.component.base.TankComponentFiltered.IFluidFilter;
import net.minecraftforge.fluids.Fluid;

public class BasicFluidFilter implements IFluidFilter{
	public Fluid[] fluids;
	
	public BasicFluidFilter(Fluid ... fluids){
		this.fluids = fluids;
	}
	
	@Override
	public boolean matches(Fluid fluid) {
		for(Fluid filter : fluids){
			if(filter.getID() == fluid.getID()) return true;
		}
		return false;
	}
	
}
