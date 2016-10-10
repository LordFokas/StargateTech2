package lordfokas.naquadria.tileentity.component.tank;

import lordfokas.naquadria.tileentity.component.CapabilityComponent;
import lordfokas.naquadria.tileentity.component.IFilter;
import lordfokas.naquadria.tileentity.facing.FaceColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class TankComponent extends CapabilityComponent<IFluidHandler> /*implements ISyncedGUI.Flow*/{
	private static final int[] KEYS = new int[]{0, 1};
	@CapabilityInject(IFluidHandler.class)
	private static Capability<IFluidHandler> fluidHandlerCapability;
	protected final IFilter<FaceColor> filter;
	protected Tank tank;
	
	public TankComponent(Tank tank){
		this(tank, IFilter.ANY);
	}
	
	public TankComponent(Tank tank, IFilter<FaceColor> filter){
		this.tank = tank;
		this.filter = filter;
	}
	
	public Tank getTank(){
		return tank;
	}
	
	@Override
	public IFluidHandler getCapability(EnumFacing side){
		return filter.matches(getColor(side)) ? tank : null;
	}

	@Override
	public Capability<IFluidHandler> getCapability(){
		return fluidHandlerCapability;
	}
	
	@Override
	public NBTTagCompound serializeNBT(){
		return tank.serializeNBT();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt){
		tank.deserializeNBT(nbt);
	}
	
	/*@Override // FIXME redo GUI Sync ... shit. Fluid ids are now strings.
	public int[] getKeyArray(){
		return KEYS;
	}

	@Override
	public int getValue(int key){
		return key == 0 ? tank.getFluidAmount() : (tank.getFluid() == null ? -1 : tank.getFluid().getID());
	}
	
	@Override
	public void setValue(int key, int val){
		if(key == 0) set(-1, val);
		else set(val, -1);
	}
	
	private void set(int f, int a){
		Fluid fluid = f == -1 ? (tank.getFluid() == null ? null : tank.getFluid()) : FluidRegistry.getFluid(f);
		int amount = a == -1 ? tank.getFluidAmount() : a;
		tank.setContent(fluid == null ? null : new FluidStack(fluid, amount));
	}*/
	
	public static class Advanced extends TankComponent{
		private final IFluidHandler input, output;
		private final IFilter<FaceColor> outFilter;
		
		public Advanced(Tank tank, IFilter<FaceColor> input, IFilter<FaceColor> output){
			super(tank, input);
			this.outFilter = output;
			this.input = new Handler(tank, true);
			this.output = new Handler(tank, false);
		}

		@Override // FIXME: add handler for I+O
		public IFluidHandler getCapability(EnumFacing side){
			FaceColor color = getColor(side);
			if(filter.matches(color)) return input;
			if(outFilter.matches(color)) return output;
			return null;
		}
	}
	
	private static class Handler implements IFluidHandler{
		private final Tank tank;
		private final boolean isInput;
		
		public Handler(Tank tank, boolean isInput){
			this.tank = tank;
			this.isInput = isInput;
		}
		
		@Override
		public int fill(FluidStack resource, boolean doFill){
			return isInput ? tank.fill(resource, doFill) : 0;
		}
		
		@Override
		public FluidStack drain(FluidStack resource, boolean doDrain){
			return isInput ? null : tank.drain(resource, doDrain);
		}
		
		@Override
		public FluidStack drain(int maxDrain, boolean doDrain){
			return isInput ? null : tank.drain(maxDrain, doDrain);
		}
		
		@Override public IFluidTankProperties[] getTankProperties(){ return tank.getTankProperties(); }
	}
}
