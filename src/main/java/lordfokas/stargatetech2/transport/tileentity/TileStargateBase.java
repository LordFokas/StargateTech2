package lordfokas.stargatetech2.transport.tileentity;

import lordfokas.stargatetech2.api.bus.IBusDevice;
import lordfokas.stargatetech2.api.bus.IBusInterface;
import lordfokas.stargatetech2.api.stargate.Address;
import lordfokas.stargatetech2.api.stargate.DialError;
import lordfokas.stargatetech2.api.stargate.ITileStargateBase;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.IEnergyHandler;

public class TileStargateBase extends TileStargateRing implements ITileStargateBase, IBusDevice, IEnergyHandler{
	
	@Override
	public DialError dial(Address address, int timeout, DialMethod method) {
		TileStargate stargate = getStargate();
		if(stargate != null){
			return stargate.dial(address, timeout, method);
		}
		return DialError.UNKNOWN_LOGIC_ERROR;
	}
	
	// #########################################################
	// IBusDevice
	
	@Override
	public IBusInterface[] getInterfaces(int side) {
		if(side == 1) return null;
		TileStargate stargate = getStargate();
		if(stargate == null) return null;
		return stargate.getInterfaces(side);
	}
	
	@Override
	public int getXCoord() {
		return xCoord;
	}

	@Override
	public int getYCoord() {
		return yCoord;
	}

	@Override
	public int getZCoord() {
		return zCoord;
	}

	@Override
	public World getWorld() {
		return worldObj;
	}
	
	// #########################################################
	// IEnergyHandler
	
	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		TileStargate stargate = getStargate();
		if(stargate == null) return 0;
		else return stargate.receiveEnergy(from, maxReceive, simulate);
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		return 0;
	}

	@Override
	public boolean canInterface(ForgeDirection from) {
		TileStargate stargate = getStargate();
		if(stargate == null) return false;
		else return stargate.canInterface(from);
	}

	@Override
	public int getEnergyStored(ForgeDirection from) {
		TileStargate stargate = getStargate();
		if(stargate == null) return 0;
		else return stargate.getEnergyStored(from);
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		TileStargate stargate = getStargate();
		if(stargate == null) return 0;
		else return stargate.getMaxEnergyStored(from);
	}
}