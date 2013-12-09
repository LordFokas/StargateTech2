package stargatetech2.core.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import stargatetech2.api.stargate.Address;
import stargatetech2.api.stargate.ITileStargateBase;
import stargatetech2.common.base.BaseTileEntity;
import stargatetech2.core.network.stargate.StargateNetwork;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileStargate extends BaseTileEntity implements ITileStargateBase{
	@ClientLogic private RenderData renderData = new RenderData();
	
	@ClientLogic
	public static class RenderData{
		public static class ChevronData{
			public boolean isLit = false;
			public float position = 0.0F;
			public float dir = 0.0F;
		}
		
		public float dTheta = 0F;
		public float curr_theta = 0F;
		private ChevronData[] chevron;
		
		public RenderData(){
			chevron = new ChevronData[9];
			for(int i = 0; i < 9; i++){
				chevron[i] = new ChevronData();
				chevron[i].isLit = (i < 4 || i > 5);
			}
		}
		
		public ChevronData getChevron(int c){
			return chevron[c];
		}
	}
	
	@Override
	public void validate(){
		super.validate();
		getAddress(); // forces the Stargate Network to generate an address for this Stargate.
	}
	
	@Override
	public void updateEntity(){
		if(worldObj.isRemote){
			clientTick();
		}else{
			serverTick();
		}
	}
	
	@ServerLogic
	private void serverTick(){}
	
	@Override
	public Address getAddress(){
		return StargateNetwork.instance().getMyAddress(worldObj, xCoord, yCoord, zCoord);
	}
	
	@Override
	public boolean dial(Address address){
		return false;
	}
	
	@ClientLogic
	private void clientTick(){
		
	}
	
	@ClientLogic
	public RenderData getRenderData(){
		return renderData;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public double getMaxRenderDistanceSquared(){
		return 0x10000;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox(){
		return AxisAlignedBB.getAABBPool().getAABB(xCoord - 2, yCoord, zCoord-2, xCoord + 3, yCoord+5, zCoord+3);
	}
	
	@Override
	protected void readNBT(NBTTagCompound nbt) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void writeNBT(NBTTagCompound nbt) {
		// TODO Auto-generated method stub
	}
}