package lordfokas.naquadria.tileentity.component.base;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;

public class TankComponentFiltered extends TankComponent{
	public static interface IFluidFilter{
		public boolean matches(Fluid fluid);
	}
	
	public final IFluidFilter filter;
	
	public TankComponentFiltered(FluidTank tank, IFluidFilter filter) {
		this(tank, true, filter);
	}
	
	public TankComponentFiltered(FluidTank tank, boolean drainable, IFluidFilter filter) {
		super(tank, drainable, true);
		this.filter = filter;
	}
	
	@Override
	public boolean canFill(Fluid fluid) {
		return filter.matches(fluid);
	}
}
