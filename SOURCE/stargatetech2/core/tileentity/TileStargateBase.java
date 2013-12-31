package stargatetech2.core.tileentity;

import stargatetech2.api.bus.IBusDevice;
import stargatetech2.api.bus.IBusInterface;
import stargatetech2.api.stargate.Address;
import stargatetech2.api.stargate.ITileStargateBase;

public class TileStargateBase extends TileStargateRing implements ITileStargateBase, IBusDevice{
	
	@Override
	public boolean dial(Address address) {
		TileStargate stargate = getStargate();
		if(stargate != null){
			return stargate.dial(address);
		}
		return false;
	}

	@Override
	public IBusInterface[] getInterfaces(int side) {
		if(side == 1) return null;
		TileStargate stargate = getStargate();
		if(stargate == null) return null;
		return stargate.getInterfaces(side);
	}
}