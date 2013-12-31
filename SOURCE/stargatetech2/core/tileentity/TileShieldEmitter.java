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
import stargatetech2.api.StargateTechAPI;
import stargatetech2.api.bus.IBusDevice;
import stargatetech2.api.bus.IBusDriver;
import stargatetech2.api.bus.IBusInterface;
import stargatetech2.api.shields.IShieldable;
import stargatetech2.api.shields.ITileShieldEmitter;
import stargatetech2.api.shields.ShieldPermissions;
import stargatetech2.common.base.BaseTileEntity;
import stargatetech2.common.util.Vec3Int;
import stargatetech2.core.ModuleCore;
import stargatetech2.core.block.BlockShield;
import stargatetech2.core.block.BlockShieldEmitter;
import stargatetech2.core.network.bus.BusDriver;
import stargatetech2.core.util.IonizedParticles;
import stargatetech2.core.util.WeakBlockRegistry;

public class TileShieldEmitter extends BaseTileEntity implements IFluidHandler, ITileShieldEmitter, IBusDevice {
	public static int MAX_EMITTER_RANGE;
	
	// NBT DATA
	private TileShieldEmitter pair = null;
	private FluidTank tank = new FluidTank(2000);
	private ShieldPermissions permissions = ShieldPermissions.getDefault();
	private boolean shieldOn = false;
	private int ionCost = 1;
	private String owner;
	
	// NORMAL FIELDS
	private Vec3Int nbtPair;
	private IBusDriver networkDriver = new BusDriver();
	private IBusInterface[] interfaces = new IBusInterface[]{
			StargateTechAPI.api().getFactory().getIBusInterface(this, networkDriver)
	};
	
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
				}else{
					if(shieldOn){
						destroyShield();
					}
				}
			}
		}
	}

	@Override
	public ShieldPermissions getPermissions(){
		return permissions.deepClone();
	}
	
	@Override
	public boolean isShieldOn(){
		return shieldOn;
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
		for(int i = 0; i <= MAX_EMITTER_RANGE; i++){
			pos = pos.offset(dir);
			int bid = worldObj.getBlockId(pos.x, pos.y, pos.z);
			if(Block.blocksList[bid] instanceof BlockShield){
				worldObj.setBlock(pos.x, pos.y, pos.z, 0, 0, 2);
			}else if(Block.blocksList[bid] instanceof IShieldable){
				((IShieldable)Block.blocksList[bid]).onUnshield(worldObj, pos.x, pos.y, pos.z);
			}else if(Block.blocksList[bid] instanceof BlockShieldEmitter){
				break;
			}
		}
		shieldOn = false;
		if(pair != null){
			pair.shieldOn = false;
		}
	}
	
	private void createShield(){
		ForgeDirection dir = ForgeDirection.getOrientation(getBlockMetadata());
		Vec3Int emitter = new Vec3Int(xCoord, yCoord, zCoord);
		Vec3Int pos = new Vec3Int(xCoord, yCoord, zCoord);
		for(int i = 0; i <= MAX_EMITTER_RANGE; i++){
			pos = pos.offset(dir);
			int bid = worldObj.getBlockId(pos.x, pos.y, pos.z);
			int meta = worldObj.getBlockMetadata(pos.x, pos.y, pos.z);
			if(WeakBlockRegistry.isRemovable(bid, meta) || worldObj.isAirBlock(pos.x, pos.y, pos.z)){
				worldObj.setBlock(pos.x, pos.y, pos.z, ModuleCore.shield.blockID, 0, 2);
				TileEntity te = worldObj.getBlockTileEntity(pos.x,  pos.y, pos.z);
				if(te instanceof TileShield){
					((TileShield)te).setEmitter(emitter);
				}
			}else if(Block.blocksList[bid] instanceof IShieldable){
				((IShieldable)Block.blocksList[bid]).onShield(worldObj, pos.x, pos.y, pos.z, xCoord, yCoord, zCoord);
			}else if(Block.blocksList[bid] instanceof BlockShieldEmitter){
				break;
			}
		}
		shieldOn = true;
		if(pair != null){
			pair.shieldOn = true;
		}
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
		for(int i = 0; i <= MAX_EMITTER_RANGE; i++){
			pos = pos.offset(dir);
			int bid = worldObj.getBlockId(pos.x, pos.y, pos.z);
			int meta = worldObj.getBlockMetadata(pos.x, pos.y, pos.z);
			Block block = Block.blocksList[bid];
			boolean ignorable = block instanceof IShieldable || block instanceof BlockShield;
			if(WeakBlockRegistry.isRemovable(bid, meta) || worldObj.isAirBlock(pos.x, pos.y, pos.z) || ignorable){
				continue;
			}else if(Block.blocksList[bid] instanceof BlockShieldEmitter){
				TileEntity te = worldObj.getBlockTileEntity(pos.x, pos.y, pos.z);
				if(te instanceof TileShieldEmitter){
					TileShieldEmitter tse = (TileShieldEmitter) te;
					if(meta == target.ordinal()){
						pair = tse;
						pair.permissions = permissions.deepClone();
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
	
	@Override
	public void updatePermissions(boolean isAllow, int flag){
		updatePermissions(false, isAllow, flag, null);
	}
	
	@Override
	public void updateExceptions(boolean isAdding, String player){
		updatePermissions(true, isAdding, 0, player);
	}
	
	@Override
	public void setOwner(String owner){
		this.owner = owner;
	}
	
	@Override
	public String getOwner(){
		return this.owner;
	}
	
	@Override
	public boolean canAccess(String player){
		return owner == null || player.contentEquals(owner);
	}
	
	private void updatePermissions(boolean isException, boolean set, int flag, String player){
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
		if(pair != null){
			pair.setPermissions(permissions);
		}
		updateClients();
		chainPermissions(1);
		chainPermissions(-1);
	}
	
	private void chainPermissions(int chainDirection){
		TileEntity te = worldObj.getBlockTileEntity(xCoord, yCoord + chainDirection, zCoord);
		if(te instanceof TileShieldEmitter && te.getBlockMetadata() == getBlockMetadata()){
			((TileShieldEmitter)te).setPermissions(permissions, chainDirection);
		}
	}
	
	private void setPermissions(ShieldPermissions sp, int chainDirection){
		setPermissions(sp);
		if(pair != null){
			pair.setPermissions(sp);
		}
		chainPermissions(chainDirection);
	}
	
	private void setPermissions(ShieldPermissions sp){
		permissions = sp.deepClone();
		updateClients();
	}
	
	@Override
	protected void readNBT(NBTTagCompound nbt) {
		NBTTagCompound tankNBT = nbt.getCompoundTag("tank");
		NBTTagCompound permNBT = nbt.getCompoundTag("perm");
		tank.readFromNBT(tankNBT);
		permissions = ShieldPermissions.readFromNBT(permNBT);
		shieldOn = nbt.getBoolean("shieldOn");
		owner = nbt.getString("owner");
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
		nbt.setString("owner", owner);
		if(pair != null){
			Vec3Int p = new Vec3Int(pair.xCoord, pair.yCoord, pair.xCoord);
			nbt.setCompoundTag("pair", p.toNBT());
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

	@Override
	public IBusInterface[] getInterfaces(int side) {
		return interfaces;
	}
}