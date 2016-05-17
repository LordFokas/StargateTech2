package lordfokas.naquadria.tileentity.component.tank;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class Tank implements IFluidHandler, INBTSerializable<NBTTagCompound>{
	private final int maxCapacity;
	private FluidStack fluidStack;
	
	public Tank(int maxCapacity){
		this.maxCapacity = maxCapacity;
	}
	
	@Override
	public int fill(EnumFacing _, FluidStack resource, boolean doFill){
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
	public FluidStack drain(EnumFacing _, FluidStack resource, boolean doDrain){
		if(resource.isFluidEqual(fluidStack))
			return drain(_, resource.amount, doDrain);
		return null;
	}
	
	@Override
	public FluidStack drain(EnumFacing _, int maxDrain, boolean doDrain){
		if(fluidStack == null) return null;
		FluidStack local = fluidStack.copy();
		FluidStack drained = local.copy();
		int amount = Math.min(maxDrain, local.amount);
		local.amount -= amount;
		drained.amount = amount;
		if(doDrain) fluidStack = local;
		return drained;
	}

	@Override public boolean canFill(EnumFacing _, Fluid fluid) { return true; }
	@Override public boolean canDrain(EnumFacing _, Fluid fluid) { return true; }
	
	@Override
	public FluidTankInfo[] getTankInfo(EnumFacing _){
		return new FluidTankInfo[]{new FluidTankInfo(fluidStack, maxCapacity)};
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
}
