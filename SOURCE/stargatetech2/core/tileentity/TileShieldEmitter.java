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
import stargatetech2.core.ModuleCore;
import stargatetech2.core.block.BlockShield;
import stargatetech2.core.block.BlockShieldEmitter;
import stargatetech2.core.util.IonizedParticles;
import stargatetech2.core.util.ShieldPermissions;

public class TileShieldEmitter extends BaseTileEntity implements IFluidHandler {
	// NBT DATA
	private TileShieldEmitter pair = null;
	private FluidTank tank = new FluidTank(64000);
	private ShieldPermissions permissions = ShieldPermissions.getDefault();
	private boolean shieldOn = false;
	private int ionCost = 1;
	
	// NORMAL FIELDS
	private Vec3Int nbtPair;
	
	@Override
	public void invalidate(){
		super.invalidate();
		if(pair != null){
			pair.pair = null;
		}
		destroyShield();
	}
	
	@Override
	public void updateEntity(){
		if(worldObj.isRemote == false && worldObj.getWorldTime() % 20 == 0){
			if(nbtPair != null){
				TileEntity te = worldObj.getBlockTileEntity(nbtPair.x, nbtPair.y, nbtPair.z);
				if(te instanceof TileShieldEmitter){
					pair = (TileShieldEmitter) te;
				}
				nbtPair = null;
			}
			if(pair == null){
				if(shieldOn){
					destroyShield();
				}
				searchPair();
			}
			if(pair != null){
				int used = useIonsFromPair(ionCost);
				if(used != 0){
					if(!shieldOn){
						createShield();
					}
					// TODO: fix this temporary workaround.
					updateClients();
				}else{
					if(shieldOn){
						destroyShield();
					}
				}
			}
		}
	}

	public ShieldPermissions getPermissions(){
		return permissions;
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
		ForgeDirection dir = ForgeDirection.getOrientation(getBlockMetadata());
		Vec3Int pos = new Vec3Int(xCoord, yCoord, zCoord);
		for(int i = 0; i <= 5; i++){
			pos = pos.offset(dir);
			int bid = worldObj.getBlockId(pos.x, pos.y, pos.z);
			if(Block.blocksList[bid] instanceof BlockShield){
				worldObj.setBlock(pos.x, pos.y, pos.z, 0, 0, 2);
			}else if(Block.blocksList[bid] instanceof BlockShieldEmitter){
				break;
			}
		}
		shieldOn = false;
		if(pair != null){
			pair.shieldOn = false;
			pair.updateClients();
		}
		updateClients();
	}
	
	private void createShield(){
		ForgeDirection dir = ForgeDirection.getOrientation(getBlockMetadata());
		Vec3Int emitter = new Vec3Int(xCoord, yCoord, zCoord);
		Vec3Int pos = new Vec3Int(xCoord, yCoord, zCoord);
		for(int i = 0; i <= 5; i++){
			pos = pos.offset(dir);
			int bid = worldObj.getBlockId(pos.x, pos.y, pos.z);
			int meta = worldObj.getBlockMetadata(pos.x, pos.y, pos.z);
			if(ShieldRegistry.isRemovable(bid, meta)){
				worldObj.setBlock(pos.x, pos.y, pos.z, ModuleCore.shield.blockID, 0, 2);
				TileEntity te = worldObj.getBlockTileEntity(pos.x,  pos.y, pos.z);
				if(te instanceof TileShield){
					((TileShield)te).setEmitter(emitter);
				}
			}else if(Block.blocksList[bid] instanceof BlockShieldEmitter){
				break;
			}
		}
		shieldOn = true;
		if(pair != null){
			pair.shieldOn = true;
			pair.updateClients();
		}
		updateClients();
	}
	
	public int getIonAmount(){
		FluidStack fs = tank.getInfo().fluid;
		return (fs == null) ? 0 : fs.amount;
	}
	
	public void setIonAmount(int ions){
		tank.setFluid(new FluidStack(IonizedParticles.fluid, ions));
	}
	
	public void searchPair(){
		ForgeDirection dir = ForgeDirection.getOrientation(getBlockMetadata());
		ForgeDirection target = dir.getOpposite();
		Vec3Int pos = new Vec3Int(xCoord, yCoord, zCoord);
		for(int i = 0; i <= 5; i++){
			pos = pos.offset(dir);
			int bid = worldObj.getBlockId(pos.x, pos.y, pos.z);
			int meta = worldObj.getBlockMetadata(pos.x, pos.y, pos.z);
			if(ShieldRegistry.isRemovable(bid, meta) || Block.blocksList[bid] instanceof IShieldable){
				continue;
			}else if(Block.blocksList[bid] instanceof BlockShieldEmitter){
				TileEntity te = worldObj.getBlockTileEntity(pos.x, pos.y, pos.z);
				if(te instanceof TileShieldEmitter){
					TileShieldEmitter tse = (TileShieldEmitter) te;
					if(meta == target.ordinal()){
						pair = tse;
						tse.pair = this;
						updateClients();
						tse.updateClients();
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
	
	public void updatePermissions(boolean set, int flag){
		doUpdatePermissions(false, set, flag, null);
		if(pair != null){
			pair.doUpdatePermissions(false, set, flag, null);
		}
	}
	
	public void updatePermissions(boolean set, String player){
		doUpdatePermissions(true, set, 0, player);
		if(pair != null){
			pair.doUpdatePermissions(true, set, 0, player);
		}
	}
	
	private void doUpdatePermissions(boolean isException, boolean set, int flag, String player){
		if(isException){
			if(set){
				permissions.setPlayerException(player);
			}else{
				permissions.removePlayerException(player);
			}
		}else{
			if(set){
				permissions.allow(flag);
			}else{
				permissions.disallow(flag);
			}
		}
		updateClients();
	}
	
	@Override
	protected void readNBT(NBTTagCompound nbt) {
		NBTTagCompound tankNBT = nbt.getCompoundTag("tank");
		NBTTagCompound permNBT = nbt.getCompoundTag("perm");
		tank.readFromNBT(tankNBT);
		permissions = ShieldPermissions.readFromNBT(permNBT);
		shieldOn = nbt.getBoolean("shieldOn");
		if(nbt.hasKey("pair")){
			nbtPair = Vec3Int.fromNBT(nbt.getCompoundTag("pair"));
		}
	}

	@Override
	protected void writeNBT(NBTTagCompound nbt) {
		NBTTagCompound tankNBT = new NBTTagCompound();
		tank.writeToNBT(tankNBT);
		nbt.setCompoundTag("tank", tankNBT);
		nbt.setCompoundTag("perm", permissions.writeToNBT());
		nbt.setBoolean("shieldOn", shieldOn);
		if(pair != null){
			Vec3Int p = new Vec3Int(pair.xCoord, pair.yCoord, pair.xCoord);
			nbt.setCompoundTag("pair", p.toNBT());
		}
	}
	
	/* IFluidHandler */
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		if(resource.fluidID == IonizedParticles.fluid.getID()){
			int f = tank.fill(resource, doFill);
			updateClients();
			return f;
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