package lordfokas.stargatetech2.lib.tileentity.component.base;

import lordfokas.stargatetech2.lib.tileentity.component.SidedComponent;
import lordfokas.stargatetech2.lib.tileentity.component.access.ITankComponent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidTank;

public class TankComponent extends SidedComponent implements ITankComponent{
	protected FluidTank tank;
	
	public TankComponent(FluidTank tank){
		this.tank = tank;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		tank.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		return tank.writeToNBT(nbt);
	}
}
