package lordfokas.naquadria.tileentity.component.tank;

import lordfokas.naquadria.tileentity.component.IFilter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class Tank implements IFluidHandler, INBTSerializable<NBTTagCompound>{
	private final int maxCapacity;
	private FluidStack fluidStack;
	
	public Tank(int maxCapacity){
		this.maxCapacity = maxCapacity;
	}
	
	// *** ACTUAL USEFUL METHODS *** *** ***
	
	public int getFluidAmount(){
		return fluidStack == null ? 0 : fluidStack.amount;
	}
	
	public void setFluidAmount(int amount){
		if(fluidStack != null) fluidStack.amount = amount;
	}
	
	public Fluid getFluid(){
		return fluidStack == null ? null : fluidStack.getFluid();
	}
	
	public FluidStack getContent(){
		return fluidStack;
	}
	
	public void setContent(FluidStack stack){
		fluidStack = stack;
	}
	
	// *** *** *** *** *** *** *** *** *** ***
	
	@Override
	public int fill(FluidStack resource, boolean doFill){
		FluidStack local = (fluidStack == null) ? null : fluidStack.copy();
		int filled = 0;
		if(local == null){
			local = new FluidStack(resource.getFluid(), Math.min(maxCapacity, resource.amount));
			filled = local.amount;
		}else if(local.isFluidEqual(resource)){
			int max = maxCapacity - local.amount;
			filled = Math.min(max, resource.amount);
			local.amount += filled;
		}
		if(doFill) fluidStack = local;
		return filled;
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain){
		if(resource.isFluidEqual(fluidStack))
			return drain(resource.amount, doDrain);
		return null;
	}
	
	@Override
	public FluidStack drain(int maxDrain, boolean doDrain){
		if(fluidStack == null) return null;
		FluidStack local = fluidStack.copy();
		FluidStack drained = local.copy();
		int amount = Math.min(maxDrain, local.amount);
		local.amount -= amount;
		drained.amount = amount;
		if(doDrain) fluidStack = local;
		return drained;
	}
	
	@Override
	public IFluidTankProperties[] getTankProperties() {
		return new IFluidTankProperties[]{ new FluidTankProperties(fluidStack, maxCapacity) };
	}

	@Override
	public NBTTagCompound serializeNBT(){
		NBTTagCompound nbt = new NBTTagCompound();
		if(fluidStack != null && fluidStack.amount > 0){
			fluidStack.writeToNBT(nbt);
		}
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt){
		fluidStack = FluidStack.loadFluidStackFromNBT(nbt);
	}
	
	public static class Filtered extends Tank{
		private final IFilter<Fluid> filter;
		
		public Filtered(int maxCapacity, IFilter<Fluid> filter) {
			super(maxCapacity);
			this.filter = filter;
		}
		
		@Override
		public int fill(FluidStack resource, boolean doFill) {
			if(!filter.matches(resource.getFluid())) return 0;
			return super.fill(resource, doFill);
		}
	}
}
