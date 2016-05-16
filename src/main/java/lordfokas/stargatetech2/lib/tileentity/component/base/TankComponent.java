package lordfokas.stargatetech2.lib.tileentity.component.base;

import lordfokas.stargatetech2.lib.tileentity.ISyncedGUI;
import lordfokas.stargatetech2.lib.tileentity.component.CapabilityComponent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;

public class TankComponent extends CapabilityComponent implements ISyncedGUI.Flow{
	private static final int[] KEYS = new int[]{0, 1};
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

	@Override
	public int[] getKeyArray() {
		return KEYS;
	}

	@Override
	public int getValue(int key) {
		return key == 0 ? tank.getFluidAmount() : (tank.getFluid() == null ? -1 : tank.getFluid().getFluid().getID());
	}

	@Override
	public void setValue(int key, int val) {
		if(key == 0) set(-1, val);
		else set(val, -1);
	}
	
	private void set(int f, int a){
		Fluid fluid = f == -1 ? (tank.getFluid() == null ? null : tank.getFluid().getFluid()) : FluidRegistry.getFluid(f);
		int amount = a == -1 ? tank.getFluidAmount() : a;
		tank.setFluid(fluid == null ? null : new FluidStack(fluid, amount));
	}
}
