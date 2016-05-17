package lordfokas.naquadria.tileentity.component.tank;

import lordfokas.naquadria.tileentity.ISyncedGUI;
import lordfokas.naquadria.tileentity.component.CapabilityComponent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

public class TankComponent extends CapabilityComponent<IFluidHandler> implements ISyncedGUI.Flow{
	private static final int[] KEYS = new int[]{0, 1};
	@CapabilityInject(IFluidHandler.class)
	private static Capability<IFluidHandler> fluidHandlerCapability;
	protected Tank tank;
	
	public TankComponent(Tank tank){
		this.tank = tank;
	}
	
	@Override
	public IFluidHandler getCapability(EnumFacing side) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Capability<IFluidHandler> getCapability() {
		return fluidHandlerCapability;
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
