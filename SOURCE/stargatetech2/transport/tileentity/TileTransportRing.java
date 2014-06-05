package stargatetech2.transport.tileentity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import stargatetech2.StargateTech2;
import stargatetech2.api.StargateTechAPI;
import stargatetech2.api.bus.IBusDevice;
import stargatetech2.api.bus.IBusInterface;
import stargatetech2.core.base.BaseTileEntity;
import stargatetech2.core.util.Vec3Int;
import stargatetech2.transport.ModuleTransport;
import stargatetech2.transport.bus.TransportRingBusDriver;

public class TileTransportRing extends BaseTileEntity implements IBusDevice{
	@ClientLogic private static Vec3Int LAST_IN_RANGE;
	@ClientLogic public final static int RING_MOV = 5;
	@ClientLogic public final static int PAUSE = 30;
	@ClientLogic public final static int HLF_PAUSE = PAUSE / 2;
	@ClientLogic public final static int SEQ_TIME = 10 * RING_MOV + PAUSE;
	@ClientLogic public final static int HLF_TIME = SEQ_TIME / 2;
	@ClientLogic public final RingRenderData renderData = new RingRenderData();
	@ServerLogic public static final int TP_COOLDOWN = 100;
	@ServerLogic private static final ArrayList<Vec3Int> RING_BLOCKS = new ArrayList<Vec3Int>(36);
	@ServerLogic private Vec3Int pairUp;
	@ServerLogic private Vec3Int pairDn;
	@ServerLogic private int yOffset;
	@ServerLogic private List<Entity> targets;
	private AxisAlignedBB myAABB;
	private int teleportCooldown = 0;
	private boolean isTeleporting = false;
	private int teleportCountdown = 0;
	private TransportRingBusDriver networkDriver = new TransportRingBusDriver(this);
	private IBusInterface[] interfaces = new IBusInterface[]{
			StargateTechAPI.api().getFactory().getIBusInterface(this, networkDriver)
	};
	
	@ClientLogic
	public static TileTransportRing getRingsInRange(World world){
		if(world != null && LAST_IN_RANGE != null){
			TileEntity te = world.getBlockTileEntity(LAST_IN_RANGE.x, LAST_IN_RANGE.y, LAST_IN_RANGE.z);
			if(te instanceof TileTransportRing){
				if(((TileTransportRing)te).isLocalPlayerInRange()){
					return (TileTransportRing)te;
				}
			}
		}
		return null;
	}
	
	@Override
	public void validate(){
		super.validate();
		refreshAABB();
	}
	
	@Override
	public void invalidate(){
		super.invalidate();
		TileTransportRing up, dn;
		up = getPair(pairUp);
		dn = getPair(pairDn);
		if(up != null){
			up.pairDn = pairDn;
		}
		if(dn != null){
			dn.pairUp = pairUp;
		}
	}
	
	@ServerLogic
	private TileTransportRing getPair(Vec3Int pair){
		if(pair != null){
			TileEntity te = worldObj.getBlockTileEntity(pair.x, pair.y, pair.z);
			if(te instanceof TileTransportRing){
				return (TileTransportRing)te;
			}
		}
		return null;
	}
	
	@Override
	public void updateEntity(){
		if(isTeleporting){
			teleportCountdown--;
		}
		if(teleportCooldown > 0){
			teleportCooldown--;
		}
		if(worldObj.isRemote){
			clientTick();
		}else{
			serverTick();
		}
	}
	
	@ServerLogic
	private void serverTick(){
		if(isTeleporting){
			if(teleportCountdown == HLF_TIME +1 ){
				selectTargets();
			}else if(teleportCountdown == HLF_TIME){
				doTeleport();
			} else if(teleportCountdown == 0){
				finishTeleportSequence();
			}
		}
	}
	
	@ServerLogic
	public boolean canActivate(){
		return teleportCooldown == 0 && !isTeleporting;
	}
	
	@ServerLogic
	public void teleport(boolean up, int leap){
		if(canActivate()){
			TileTransportRing dest = this;
			for(int i = 0; i < leap; i++){
				Vec3Int pair = up ? dest.pairUp : dest.pairDn;
				if(pair == null) return;
				TileEntity te = worldObj.getBlockTileEntity(pair.x, pair.y, pair.z);
				if(te instanceof TileTransportRing){
					dest = (TileTransportRing) te;
				}else{
					return;
				}
			}
			if(dest != this && dest.canActivate()){
				int off = dest.yCoord - yCoord;
				this.startTeleportSequence( off);
				dest.startTeleportSequence(-off);
			}
		}
	}
	
	@ServerLogic
	private void startTeleportSequence(int offset){
		yOffset = offset;
		isTeleporting = true;
		teleportCountdown = SEQ_TIME;
		updateClients();
		for(Vec3Int b : RING_BLOCKS){
			if(worldObj.isAirBlock(xCoord + b.x, yCoord + b.y, zCoord + b.z)){
				worldObj.setBlock(xCoord + b.x, yCoord + b.y, zCoord + b.z, ModuleTransport.invisible.blockID, 14, 3);
			}
		}
	}
	
	@ServerLogic
	private void selectTargets(){
		targets = worldObj.getEntitiesWithinAABB(Entity.class, myAABB);
	}
	
	@ServerLogic
	private void doTeleport(){
		if(targets == null) return;
		for(Entity entity : targets){
			if(!entity.isDead){
				if(entity instanceof EntityPlayerMP){
					((EntityPlayerMP)entity).setPositionAndUpdate(entity.posX, entity.posY + yOffset, entity.posZ);
				}else{
					entity.setPosition(entity.posX, entity.posY + yOffset, entity.posZ);
				}
			}
		}
	}
	
	@ServerLogic
	private void finishTeleportSequence(){
		isTeleporting = false;
		teleportCooldown = TP_COOLDOWN;
		updateClients();
		for(Vec3Int b : RING_BLOCKS){
			if(worldObj.getBlockId(xCoord + b.x, yCoord + b.y, zCoord + b.z) == ModuleTransport.invisible.blockID){
				worldObj.setBlock(xCoord + b.x, yCoord + b.y, zCoord + b.z, 0);
			}
		}
	}
	
	@ServerLogic
	public void link(){
		Vec3Int me0 = new Vec3Int(xCoord, yCoord, zCoord);
		Vec3Int me1 = me0.offset(ForgeDirection.UNKNOWN);
		for(int y = yCoord - 10; y > 0; y--){
			if(findPair(me0, y, false)){
				y = 0;
			}
		}
		int max = worldObj.getHeight();
		for(int y = yCoord + 10; y < max; y++){
			if(findPair(me1, y, true)){
				y = max;
			}
		}
	}
	
	@ServerLogic
	private boolean findPair(Vec3Int me, int y, boolean dirUp){
		TileEntity te = worldObj.getBlockTileEntity(xCoord, y, zCoord);
		if(te instanceof TileTransportRing){
			if(dirUp){
				((TileTransportRing)te).pairDn = me;
				pairUp = new Vec3Int(xCoord, y, zCoord);
			}else{
				((TileTransportRing)te).pairUp = me;
				pairDn = new Vec3Int(xCoord, y, zCoord);
			}
			return true;
		}
		return false;
	}
	
	@ClientLogic
	private void clientTick(){
		if(isTeleporting){
			if(teleportCountdown >= HLF_TIME + HLF_PAUSE){
				renderData.ringPos[renderData.movingRing]++;
				if(renderData.ringPos[renderData.movingRing] == RING_MOV && renderData.movingRing > 0){
					renderData.movingRing--;
				}
				renderData.mode = 1.0F;
			}else if(teleportCountdown <= HLF_TIME - HLF_PAUSE){
				renderData.ringPos[renderData.movingRing]--;
				if(renderData.ringPos[renderData.movingRing] == 0 && renderData.movingRing < 4){
					renderData.movingRing++;
				}
				renderData.mode = -1.0F;
			}
		}
		if(isLocalPlayerInRange()){
			LAST_IN_RANGE = new Vec3Int(xCoord, yCoord, zCoord);
		}
	}
	
	@ClientLogic
	private boolean isLocalPlayerInRange(){
		return StargateTech2.proxy.isLocalPlayerInAABB(worldObj, myAABB);
	}
	
	private void refreshAABB(){
		myAABB = AxisAlignedBB.getBoundingBox(xCoord - 1, yCoord + 2, zCoord - 1, xCoord + 2, yCoord + 5, zCoord + 2);
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox(){
		return AxisAlignedBB.getAABBPool().getAABB(xCoord-2, yCoord, zCoord-2, xCoord+3, yCoord+5, zCoord+3);
	}
	
	@Override
	public double getMaxRenderDistanceSquared() {
		return 0x4000;
	}
	
	@Override
	protected void readNBT(NBTTagCompound nbt) {
		renderData.isRendering = isTeleporting = nbt.getBoolean("isTeleporting");
		teleportCountdown = nbt.getInteger("teleportCountdown");
		teleportCooldown = nbt.getInteger("teleportCooldown");
		yOffset = nbt.getInteger("yOffset");
		if(nbt.hasKey("pairUp")){
			pairUp = Vec3Int.fromNBT(nbt.getCompoundTag("pairUp"));
		}else{
			pairUp = null;
		}
		if(nbt.hasKey("pairDn")){
			pairDn = Vec3Int.fromNBT(nbt.getCompoundTag("pairDn"));
		}else{
			pairDn = null;
		}
		if(isTeleporting){
			renderData.movingRing = 4;
			renderData.ringPos = new int[5];
		}
		refreshAABB();
	}
	
	@Override
	protected void writeNBT(NBTTagCompound nbt) {
		nbt.setBoolean("isTeleporting", isTeleporting);
		nbt.setInteger("teleportCountdown", teleportCountdown);
		nbt.setInteger("teleportCooldown", teleportCooldown);
		nbt.setInteger("yOffset", yOffset);
		if(pairUp != null){
			nbt.setCompoundTag("pairUp", pairUp.toNBT());
		}
		if(pairDn != null){
			nbt.setCompoundTag("pairDn", pairDn.toNBT());
		}
	}
	
	@ClientLogic
	public class RingRenderData{
		public boolean isRendering = false;
		public int movingRing;
		public int ringPos[];
		public float mode;
	}
	
	static{
		RING_BLOCKS.add(new Vec3Int(-2, 2, -1));
		RING_BLOCKS.add(new Vec3Int(-2, 2,  0));
		RING_BLOCKS.add(new Vec3Int(-2, 2,  1));
		RING_BLOCKS.add(new Vec3Int( 2, 2, -1));
		RING_BLOCKS.add(new Vec3Int( 2, 2,  0));
		RING_BLOCKS.add(new Vec3Int( 2, 2,  1));
		RING_BLOCKS.add(new Vec3Int(-1, 2, -2));
		RING_BLOCKS.add(new Vec3Int( 0, 2, -2));
		RING_BLOCKS.add(new Vec3Int( 1, 2, -2));
		RING_BLOCKS.add(new Vec3Int(-1, 2,  2));
		RING_BLOCKS.add(new Vec3Int( 0, 2,  2));
		RING_BLOCKS.add(new Vec3Int( 1, 2,  2));
		RING_BLOCKS.add(new Vec3Int(-2, 3, -1));
		RING_BLOCKS.add(new Vec3Int(-2, 3,  0));
		RING_BLOCKS.add(new Vec3Int(-2, 3,  1));
		RING_BLOCKS.add(new Vec3Int( 2, 3, -1));
		RING_BLOCKS.add(new Vec3Int( 2, 3,  0));
		RING_BLOCKS.add(new Vec3Int( 2, 3,  1));
		RING_BLOCKS.add(new Vec3Int(-1, 3, -2));
		RING_BLOCKS.add(new Vec3Int( 0, 3, -2));
		RING_BLOCKS.add(new Vec3Int( 1, 3, -2));
		RING_BLOCKS.add(new Vec3Int(-1, 3,  2));
		RING_BLOCKS.add(new Vec3Int( 0, 3,  2));
		RING_BLOCKS.add(new Vec3Int( 1, 3,  2));
		RING_BLOCKS.add(new Vec3Int(-2, 4, -1));
		RING_BLOCKS.add(new Vec3Int(-2, 4,  0));
		RING_BLOCKS.add(new Vec3Int(-2, 4,  1));
		RING_BLOCKS.add(new Vec3Int( 2, 4, -1));
		RING_BLOCKS.add(new Vec3Int( 2, 4,  0));
		RING_BLOCKS.add(new Vec3Int( 2, 4,  1));
		RING_BLOCKS.add(new Vec3Int(-1, 4, -2));
		RING_BLOCKS.add(new Vec3Int( 0, 4, -2));
		RING_BLOCKS.add(new Vec3Int( 1, 4, -2));
		RING_BLOCKS.add(new Vec3Int(-1, 4,  2));
		RING_BLOCKS.add(new Vec3Int( 0, 4,  2));
		RING_BLOCKS.add(new Vec3Int( 1, 4,  2));
	}

	@Override
	public IBusInterface[] getInterfaces(int side) {
		return side == 1 ? null : interfaces;
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
}