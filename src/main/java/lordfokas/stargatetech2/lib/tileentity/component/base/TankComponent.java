package lordfokas.stargatetech2.lib.tileentity.component.base;

import lordfokas.stargatetech2.lib.tileentity.component.SidedComponent;
import lordfokas.stargatetech2.lib.tileentity.component.access.ITankComponent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;

public class TankComponent extends SidedComponent implements ITankComponent{
	protected boolean drainable, fillable;
	protected FluidTank tank;
	
	public TankComponent(FluidTank tank){
		this(tank, true, true);
	}
	
	public TankComponent(FluidTank tank, boolean drainable, boolean fillable){
		this.tank = tank;
		this.drainable = drainable;
		this.fillable = fillable;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		tank.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		return tank.writeToNBT(nbt);
	}

	@Override
	public boolean canDrain() {
		return drainable;
	}

	@Override
	public boolean canFill(Fluid fluid) {
		return fillable;
	}

	@Override
	public FluidStack drain(int amount, boolean doDrain) {
		return tank.drain(amount, doDrain);
	}

	@Override
	public int fill(FluidStack stack, boolean doFill) {
		if(!canFill(stack.getFluid())) return 0;
		return tank.fill(stack, doFill);
	}

	@Override
	public FluidTankInfo getInfo() {
		return tank.getInfo();
	}
}
