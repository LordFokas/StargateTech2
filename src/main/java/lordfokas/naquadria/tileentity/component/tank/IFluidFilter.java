package lordfokas.naquadria.tileentity.component.tank;

import net.minecraftforge.fluids.Fluid;

public interface IFluidFilter {
	public boolean matches(Fluid fluid);
}
