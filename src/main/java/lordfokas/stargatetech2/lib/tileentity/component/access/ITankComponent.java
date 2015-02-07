package lordfokas.stargatetech2.lib.tileentity.component.access;

import lordfokas.stargatetech2.lib.tileentity.component.IAccessibleTileComponent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

public interface ITankComponent extends IAccessibleTileComponent{
	public boolean canDrain();
	public boolean canFill(Fluid fluid);
	public FluidStack drain(int amount, boolean doDrain);
	public int fill(FluidStack stack, boolean doFill);
	public FluidTankInfo getInfo();
}