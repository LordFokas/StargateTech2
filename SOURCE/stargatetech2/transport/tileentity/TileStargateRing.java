package stargatetech2.transport.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import stargatetech2.api.stargate.Address;
import stargatetech2.api.stargate.ITileStargate;
import stargatetech2.core.base.BaseTileEntity;

public class TileStargateRing extends BaseTileEntity implements ITileStargate{
	protected int sgx, sgy, sgz;
	
	@Override
	public final Address getAddress(){
		Address address = null;
		TileStargate stargate = getStargate();
		if(stargate != null){
			address = stargate.getAddress();
		}
		return address;
	}
	
	@Override
	public void invalidate(){
		super.invalidate();
		TileStargate sg = getStargate();
		if(sg != null){
			sg.destroyStargate();
		}
	}
	
	@Override
	public boolean canUpdate(){
		return false;
	}
	
	public final TileStargate getStargate(){
		TileEntity te = worldObj.getBlockTileEntity(sgx, sgy, sgz);
		if(te instanceof TileStargate){
			return (TileStargate) te;
		}else{
			return null;
		}
	}
	
	public void setSGPosition(int x, int y, int z){
		sgx = x;
		sgy = y;
		sgz = z;
	}
	
	@Override
	protected final void readNBT(NBTTagCompound nbt){
		sgx = nbt.getInteger("sgx");
		sgy = nbt.getInteger("sgy");
		sgz = nbt.getInteger("sgz");
	}
	
	@Override
	protected final void writeNBT(NBTTagCompound nbt){
		nbt.setInteger("sgx", sgx);
		nbt.setInteger("sgy", sgy);
		nbt.setInteger("sgz", sgz);
	}
}