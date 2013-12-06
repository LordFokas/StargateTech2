package stargatetech2.core.tileentity;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import stargatetech2.common.base.BaseTileEntity;

public class TileStargate extends BaseTileEntity {
	@ClientLogic private RenderData renderData = new RenderData();
	Random random = new Random();
	
	@ClientLogic
	public static class RenderData{
		public static class ChevronData{
			public boolean isLit = false;
			public float position = 0.0F;
			public float dir = 0.0F;
		}
		
		public float dTheta = 1.5F;
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
	public void updateEntity(){
		if(worldObj.isRemote){
			clientTick();
		}else{
			serverTick();
		}
	}
	
	@ServerLogic
	private void serverTick(){}
	
	@ClientLogic
	private void clientTick(){
		// ring rotation //
		renderData.curr_theta += renderData.dTheta;
		if(renderData.curr_theta > 360F){
			renderData.curr_theta -= 360F;
		}
		if(worldObj.getWorldTime() % 80 == 0){
			renderData.dTheta *= -1;
		}
		for(int i = 0; i < 9; i++){
			RenderData.ChevronData chevron = renderData.getChevron(i);
			chevron.position = 0.2F * ((float)((worldObj.getWorldTime() + i) % 9) / 9F);
			if(worldObj.getWorldTime() % 30 == 0){
				chevron.isLit = random.nextBoolean();
			}
		}
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