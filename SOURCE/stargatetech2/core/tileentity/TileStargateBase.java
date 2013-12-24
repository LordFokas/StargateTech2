package stargatetech2.core.tileentity;

import stargatetech2.api.stargate.Address;
import stargatetech2.api.stargate.ITileStargateBase;

public class TileStargateBase extends TileStargateRing implements ITileStargateBase{
	
	@Override
	public boolean dial(Address address) {
		TileStargate stargate = getStargate();
		if(stargate != null){
			return stargate.dial(address);
		}
		return false;
	}
}