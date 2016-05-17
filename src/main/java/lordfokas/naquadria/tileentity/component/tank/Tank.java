package lordfokas.naquadria.tileentity.component.tank;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class Tank implements IFluidHandler{
	private final int maxCapacity;
	private FluidStack fluidStack;
	
	public Tank(int maxCapacity){
		this.maxCapacity = maxCapacity;
	}
	
	@Override
	public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override public boolean canFill(EnumFacing from, Fluid fluid) { return true; }
	@Override public boolean canDrain(EnumFacing from, Fluid fluid) { return true; }
	
	@Override
	public FluidTankInfo[] getTankInfo(EnumFacing from) {
		return new FluidTankInfo[]{new FluidTankInfo(fluidStack, maxCapacity)};
	}

}
