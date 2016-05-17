package lordfokas.naquadria.tileentity.component.tank;

import lordfokas.naquadria.tileentity.ISyncedGUI;
import lordfokas.naquadria.tileentity.component.CapabilityComponent;
import lordfokas.naquadria.tileentity.facing.FaceColor;
import lordfokas.naquadria.tileentity.facing.FaceColorFilter;
import lordfokas.naquadria.tileentity.facing.IFaceColorFilter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TankComponent extends CapabilityComponent<IFluidHandler> implements ISyncedGUI.Flow{
	private static final int[] KEYS = new int[]{0, 1};
	@CapabilityInject(IFluidHandler.class)
	private static Capability<IFluidHandler> fluidHandlerCapability;
	protected final IFaceColorFilter filter;
	protected Tank tank;
	
	public TankComponent(int size){
		this(size, FaceColorFilter.ANY);
	}
	
	public TankComponent(int size, IFaceColorFilter filter){
		this.tank = new Tank(size);
		this.filter = filter;
	}
	
	public Tank getTank(){
		return tank;
	}
	
	@Override
	public IFluidHandler getCapability(EnumFacing side){
		return filter.doesColorMatch(getColor(side)) ? tank : null;
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
	
	@Override
	public int[] getKeyArray(){
		return KEYS;
	}

	@Override
	public int getValue(int key){
		return key == 0 ? tank.getFluidAmount() : (tank.getFluid() == null ? -1 : tank.getFluid().getFluid().getID());
	}
	
	@Override
	public void setValue(int key, int val){
		if(key == 0) set(-1, val);
		else set(val, -1);
	}
	
	private void set(int f, int a){
		Fluid fluid = f == -1 ? (tank.getFluid() == null ? null : tank.getFluid().getFluid()) : FluidRegistry.getFluid(f);
		int amount = a == -1 ? tank.getFluidAmount() : a;
		tank.setFluid(fluid == null ? null : new FluidStack(fluid, amount));
	}
	
	public static class Advanced extends TankComponent{
		private final IFluidHandler input, output;
		private final IFaceColorFilter outFilter;
		
		public Advanced(int size, IFaceColorFilter input, IFaceColorFilter output){
			super(size, input);
			this.outFilter = output;
			this.input = new Handler(tank, true);
			this.output = new Handler(tank, false);
		}

		@Override
		public IFluidHandler getCapability(EnumFacing side){
			FaceColor color = getColor(side);
			if(filter.doesColorMatch(color)) return input;
			if(outFilter.doesColorMatch(color)) return output;
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
		public int fill(EnumFacing _, FluidStack resource, boolean doFill){
			return isInput ? tank.fill(_, resource, doFill) : 0;
		}
		
		@Override
		public FluidStack drain(EnumFacing _, FluidStack resource, boolean doDrain){
			return isInput ? null : tank.drain(_, resource, doDrain);
		}
		
		@Override
		public FluidStack drain(EnumFacing _, int maxDrain, boolean doDrain){
			return isInput ? null : tank.drain(_, maxDrain, doDrain);
		}
		
		@Override public boolean canFill(EnumFacing _, Fluid fluid){ return isInput; }
		@Override public boolean canDrain(EnumFacing _, Fluid fluid){ return !isInput; }
		@Override public FluidTankInfo[] getTankInfo(EnumFacing _){ return tank.getTankInfo(_); }
	}
}
