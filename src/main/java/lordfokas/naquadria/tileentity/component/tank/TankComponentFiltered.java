package lordfokas.naquadria.tileentity.component.tank;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;

public class TankComponentFiltered extends TankComponent{
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
