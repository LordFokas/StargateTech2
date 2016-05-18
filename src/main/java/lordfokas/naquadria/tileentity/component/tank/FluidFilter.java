package lordfokas.naquadria.tileentity.component.tank;

import lordfokas.naquadria.tileentity.component.IFilter;
import net.minecraftforge.fluids.Fluid;

public class FluidFilter implements IFilter<Fluid>{
	protected Fluid filter;
	
	public FluidFilter(Fluid fluid){
		this.filter = fluid;
	}
	
	@Override
	public boolean matches(Fluid fluid) {
		return filter.getName().equals(fluid.getName());
	}
}
