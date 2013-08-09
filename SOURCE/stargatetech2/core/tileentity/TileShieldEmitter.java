package stargatetech2.core.tileentity;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import stargatetech2.api.IShieldable;
import stargatetech2.common.base.BaseTileEntity;
import stargatetech2.common.registry.ShieldRegistry;
import stargatetech2.common.util.Vec3Int;
import stargatetech2.core.IonizedParticles;
import stargatetech2.core.block.BlockShieldEmitter;

public class TileShieldEmitter extends BaseTileEntity implements IFluidHandler {
	private TileShieldEmitter pair = null;
	private FluidTank tank = new FluidTank(64000);
	private boolean shieldOn = false;
	private int ionCost = 500;
	private int direction = 0;
	private int pairSearchTick = 0;
	
	@Override
	public void validate(){
		super.validate();
		searchPair();
		//FluidStack stack = new FluidStack(IonizedParticles.fluid, 64000);
		//tank.fill(stack, true);
	}
	
	@Override
	public void updateEntity(){
		if(pair != null){
			if(useIonsFromPair(ionCost) != 0){
				if(!shieldOn){
					createShield();
				}
			}else{
				if(shieldOn){
					destroyShield();
				}
			}
		}else{
			if(pairSearchTick == 0){
				searchPair();
			}
			pairSearchTick = (++pairSearchTick) % 20;
		}
	}
	
	private int useIons(int ions){
		int avail = tank.getFluidAmount();
		return ions <= avail ? tank.drain(ions, true).amount : 0;
	}
	
	private int useIonsFromPair(int ions){
		int local = useIons(ions);
		return local != 0 ? local : pair.useIons(ions);
	}
	
	private void destroyShield(){
		shieldOn = false;
	}
	
	private void createShield(){
		shieldOn = true;
	}
	
	public void searchPair(){
		ForgeDirection dir = ForgeDirection.getOrientation(direction);
		ForgeDirection target = dir.getOpposite();
		Vec3Int pos = new Vec3Int(xCoord, yCoord, zCoord);
		for(int i = 0; i < 5; i++){
			pos = pos.offset(dir);
			int bid = worldObj.getBlockId(pos.x, pos.y, pos.z);
			int meta = worldObj.getBlockMetadata(pos.x, pos.y, pos.z);
			if(ShieldRegistry.isRemovable(bid, meta) || Block.blocksList[bid] instanceof IShieldable){
				continue;
			}else if(Block.blocksList[bid] instanceof BlockShieldEmitter){
				TileEntity te = worldObj.getBlockTileEntity(pos.x, pos.y, pos.z);
				if(te instanceof TileShieldEmitter){
					TileShieldEmitter tse = (TileShieldEmitter) te;
					if(tse.direction == target.ordinal()){
						pair = tse;
						tse.pair = this;
						return;
					}
				}
				pair = null;
				return;
			}else{
				pair = null;
				return;
			}
		}
	}
	
	public void updatePermissions(){ // TODO finish
		int newIonCost = 750;
		
		ionCost = newIonCost;
		if(pair != null){
			pair.ionCost = newIonCost;
		}
	}
	
	@Override
	protected void readNBT(NBTTagCompound nbt) {
		NBTTagCompound tankNBT = nbt.getCompoundTag("tank");
		tank.readFromNBT(tankNBT);
		shieldOn = nbt.getBoolean("shieldOn");
		if(nbt.hasKey("pair")){
			NBTTagCompound pairNBT = nbt.getCompoundTag("pair");
			int px, py, pz;
			px = pairNBT.getInteger("x");
			py = pairNBT.getInteger("y");
			pz = pairNBT.getInteger("z");
			TileEntity te = worldObj.getBlockTileEntity(px, py, pz);
			if(te instanceof TileShieldEmitter){
				pair = (TileShieldEmitter) te;
			}
		}
	}

	@Override
	protected void writeNBT(NBTTagCompound nbt) {
		NBTTagCompound tankNBT = new NBTTagCompound();
		tank.writeToNBT(tankNBT);
		nbt.setCompoundTag("tank", tankNBT);
		nbt.setBoolean("shieldOn", shieldOn);
		if(pair != null){
			NBTTagCompound pairNBT = new NBTTagCompound();
			pairNBT.setInteger("x", pair.xCoord);
			pairNBT.setInteger("y", pair.yCoord);
			pairNBT.setInteger("z", pair.zCoord);
			nbt.setCompoundTag("pair", pairNBT);
		}
	}
	
	/* IFluidHandler */
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		if(resource.fluidID == IonizedParticles.fluid.getID()){
			return tank.fill(resource, doFill);
		}
		return 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return fluid.getID() == IonizedParticles.fluid.getID();
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[]{tank.getInfo()};
	}
}