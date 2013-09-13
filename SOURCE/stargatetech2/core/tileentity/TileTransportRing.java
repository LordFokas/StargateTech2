package stargatetech2.core.tileentity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;
import stargatetech2.common.base.BaseTileEntity;
import stargatetech2.common.util.Vec3Int;
import stargatetech2.core.ModuleCore;

public class TileTransportRing extends BaseTileEntity {
	@ClientLogic public final static int RING_MOV = 5;
	@ClientLogic public final static int PAUSE = 30;
	@ClientLogic public final static int HLF_PAUSE = PAUSE / 2;
	@ClientLogic public final static int SEQ_TIME = 10 * RING_MOV + PAUSE;
	@ClientLogic public final static int HLF_TIME = SEQ_TIME / 2;
	@ClientLogic public final RingRenderData renderData = new RingRenderData();
	@ServerLogic private static final ArrayList<Vec3Int> RING_BLOCKS = new ArrayList<Vec3Int>(36);
	@ServerLogic private Vec3Int pairUp;
	@ServerLogic private Vec3Int pairDn;
	@ServerLogic private int yOffset;
	@ServerLogic private List<Entity> targets;
	private int teleportCooldown = 0;
	private boolean isTeleporting = false;
	private int teleportCountdown = 0;
	
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
		if(worldObj.getWorldTime() % 150 == 0){
			teleport(true);
		}
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
	public void teleport(boolean up){
		if(teleportCooldown == 0 && !isTeleporting){
			Vec3Int pair = up ? pairUp : pairDn;
			if(pair == null) return;
			TileEntity te = worldObj.getBlockTileEntity(pair.x, pair.y, pair.z);
			if(te instanceof TileTransportRing){
				TileTransportRing dest = (TileTransportRing) te;
				if(dest.teleportCooldown == 0 && !dest.isTeleporting){
					int off = pair.y - yCoord;
					this.startTeleportSequence( off);
					dest.startTeleportSequence(-off);
				}
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
				worldObj.setBlock(xCoord + b.x, yCoord + b.y, zCoord + b.z, ModuleCore.invisible.blockID);
			}
		}
	}
	
	@ServerLogic
	private void selectTargets(){
		AxisAlignedBB area = AxisAlignedBB.getAABBPool().getAABB(xCoord - 1, yCoord + 2, zCoord - 1, xCoord + 2, yCoord + 5, zCoord + 2);
		targets = worldObj.getEntitiesWithinAABB(Entity.class, area);
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
		teleportCooldown = 120;
		updateClients();
		for(Vec3Int b : RING_BLOCKS){
			if(worldObj.getBlockId(xCoord + b.x, yCoord + b.y, zCoord + b.z) == ModuleCore.invisible.blockID){
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
}