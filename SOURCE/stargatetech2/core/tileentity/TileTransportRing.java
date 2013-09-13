package stargatetech2.core.tileentity;

import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
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
	
	private int teleportCooldown = 0;
	private boolean isTeleporting = false;
	private int teleportCountdown = 0;
	
	@Override
	public void updateEntity(){
		if(isTeleporting){
			teleportCountdown--;
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
			startTeleportSequence();
		}
		if(teleportCountdown == HLF_TIME){
			doTeleport();
		} else if(teleportCountdown == 0){
			finishTeleportSequence();
		}
		if(teleportCooldown > 0){
			teleportCooldown--;
		}
	}
	
	@ServerLogic
	private void startTeleportSequence(){
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
	private void doTeleport(){
		// do something.
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
		return AxisAlignedBB.getAABBPool().getAABB(xCoord-2, yCoord, zCoord-2, xCoord+3, yCoord+4, zCoord+3);
	}
	
	@Override
	public double getMaxRenderDistanceSquared() {
		return 0x4000;
	}
	
	@Override
	protected void readNBT(NBTTagCompound nbt) {
		renderData.isRendering = isTeleporting = nbt.getBoolean("isTeleporting");
		teleportCountdown = nbt.getInteger("teleportCountdown");
		if(isTeleporting){
			renderData.movingRing = 4;
			renderData.ringPos = new int[5];
		}
	}
	
	@Override
	protected void writeNBT(NBTTagCompound nbt) {
		nbt.setBoolean("isTeleporting", isTeleporting);
		nbt.setInteger("teleportCountdown", teleportCountdown);
	}
	
	@ClientLogic
	public class RingRenderData{
		public static final int MAX_RING_POS = 10;
		
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