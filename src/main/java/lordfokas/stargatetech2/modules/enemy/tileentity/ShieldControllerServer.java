package lordfokas.stargatetech2.modules.enemy.tileentity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import lordfokas.stargatetech2.api.shields.IShieldable;
import lordfokas.stargatetech2.api.shields.ShieldPermissions;
import lordfokas.stargatetech2.lib.tileentity.IRedstoneAware;
import lordfokas.stargatetech2.lib.tileentity.ITile;
import lordfokas.stargatetech2.lib.tileentity.ITileContext;
import lordfokas.stargatetech2.modules.ModuleEnemy;
import lordfokas.stargatetech2.modules.enemy.BlockShield;
import lordfokas.stargatetech2.modules.enemy.IShieldControllerProvider;
import lordfokas.stargatetech2.modules.enemy.TileShieldEmitter;
import lordfokas.stargatetech2.util.Vec3Int;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ShieldControllerServer extends ShieldControllerCommon
implements ITileContext.Server, IShieldControllerProvider, IRedstoneAware{
	private static final int ION_DRAIN = 10;
	private ArrayList<Vec3Int> emitters = new ArrayList();
	private LinkedList<Vec3Int> shields = new LinkedList();
	private ITile.Server tile;
	private boolean redstone = false;
	
	@Override
	public void setTile(ITile.Server tile) {
		this.tile = tile;
	}
	
	@Override
	public boolean canTick() {
		return true;
	}
	
	@Override
	public void tick() {
		boolean shouldActivate = false; // TODO: this prevents the shield from activating on non-check ticks.
		boolean shouldDeactivate = false;
		
		if((tile.getWorld().getTotalWorldTime() % 100) == 0){
			if(enabled && hasIons()){
				tank.drain(ION_DRAIN, true);
				shouldActivate = true;
			}else{
				shouldDeactivate = true;
			}
		}
		
		updateShields(shouldActivate, shouldDeactivate);
	}
	
	private void updateShields(boolean shouldActivate, boolean shouldDeactivate){
		if(active && (!enabled || shouldDeactivate)){
			lowerShields();
		}
		if(enabled && !active && shouldActivate){
			raiseShields();
		}
	}
	
	private boolean hasIons(){
		return tank.getFluidAmount() >= ION_DRAIN;
	}
	
	private void raiseShields(){
		shields.clear();
		for(Vec3Int pos : emitters){ // TODO: fix this shit
			TileEntity te = tile.getWorld().getTileEntity(pos.x, pos.y, pos.z);
			if(te instanceof TileShieldEmitter){
				shields.addAll(((TileShieldEmitter)te).createShields());
			}
		}
		active = true;
	}
	
	private void lowerShields(){
		for(Vec3Int pos : shields){ // TODO: fix this shit
			Block b = tile.getWorld().getBlock(pos.x, pos.y, pos.z);
			if(b instanceof BlockShield){
				tile.getWorld().setBlockToAir(pos.x, pos.y, pos.z);
			}else if(b instanceof IShieldable){
				((IShieldable)b).onUnshield(tile.getWorld(), pos.x, pos.y, pos.z);
			}
		}
		shields.clear();
		active = false;
	}
	
	@Override
	public ShieldPermissions getPermissions(){
		return permissions;
	}
	
	@Override
	public boolean isShieldOn(){
		return active;
	}
	
	@Override
	public Vec3Int getShieldControllerCoords() {
		return new Vec3Int(tile.x(), tile.y(), tile.z());
	}
	
	@Override
	public boolean setShieldStatus(boolean enabled){
		if(!redstone){
			this.enabled = enabled;
			updateShields(hasIons(), true);
		}
		return !redstone;
	}
	
	public void addEmitter(TileShieldEmitter emitter){
		emitters.add(new Vec3Int(emitter.xCoord, emitter.yCoord, emitter.zCoord));
	}
	
	public void removeEmitter(TileShieldEmitter emitter){
		emitters.remove(new Vec3Int(emitter.xCoord, emitter.yCoord, emitter.zCoord));
		ArrayList<Vec3Int> unreachableDependencies = emitters;
		LinkedList<Vec3Int> memory = new LinkedList();
		memory.add(getShieldControllerCoords());
		emitters = new ArrayList();
		recursiveRemap(tile.getWorld(), tile.x(), tile.y(), tile.z(), emitters, memory);
		unreachableDependencies.removeAll(emitters);
		dropAll(unreachableDependencies);
	}
	
	private void dropAll(List<Vec3Int> unreachable){
		boolean shieldsUp = active;
		if(shieldsUp) lowerShields();
		for(Vec3Int e : unreachable){
			ModuleEnemy.shieldEmitter.dropSelf(tile.getWorld(), e.x, e.y, e.z);
		}
		if(shieldsUp) raiseShields();
	}
	
	private void recursiveRemap(World w, int x, int y, int z, ArrayList<Vec3Int> found, LinkedList<Vec3Int> memory){
		for(ForgeDirection fd : ForgeDirection.VALID_DIRECTIONS){
			int nx = x + fd.offsetX;
			int ny = y + fd.offsetY;
			int nz = z + fd.offsetZ;
			if(ny >= 0 && ny < w.getActualHeight()){
				Vec3Int pos = new Vec3Int(nx, ny, nz);
				if(!memory.contains(pos)){
					memory.add(pos);
					TileEntity te = w.getTileEntity(nx, ny, nz);
					if(te instanceof TileShieldEmitter){
						found.add(pos);
						recursiveRemap(w, nx, ny, nz, found, memory);
					}
				}
			}
		}
	}
	
	public void updatePermissions(boolean set, int flag){
		if(set){
			permissions.allow(flag);
		}else{
			permissions.disallow(flag);
		}
		tile.updateClients();
	}
	
	public void updateExceptions(boolean set, String name){
		if(set){
			permissions.setPlayerException(name);
		}else{
			permissions.removePlayerException(name);
		}
		tile.updateClients();
	}
	
	
	// ***************************************************
	// Named Binary Tags
	
	@Override
	public void readNBTData(NBTTagCompound nbt) {
		super.readNBTData(nbt);
		int num_emitters = nbt.getInteger("emitters");
		emitters = new ArrayList(num_emitters);
		for(int i = 0; i < num_emitters; i++){
			emitters.add(Vec3Int.fromNBT(nbt.getCompoundTag("emitter_" + i)));
		}
		int num_shields = nbt.getInteger("shields");
		shields.clear();
		for(int i = 0; i < num_shields; i++){
			shields.add(Vec3Int.fromNBT(nbt.getCompoundTag("shield_" + i)));
		}
		redstone = nbt.getBoolean("redstone");
	}
	
	@Override
	public void writeNBTData(NBTTagCompound nbt) {
		super.writeNBTData(nbt);
		nbt.setInteger("emitters", emitters.size());
		for(int i = 0; i < emitters.size(); i++){
			nbt.setTag("emitter_" + i, emitters.get(i).toNBT());
		}
		nbt.setInteger("shields", shields.size());
		for(int i = 0; i < shields.size(); i++){
			nbt.setTag("shield_" + i, shields.get(i).toNBT());
		}
		nbt.setBoolean("redstone", redstone);
	}
	
	@Override
	public void setUsesRedstone(boolean redstone){
		if(!redstone){
			onRedstoneState(false);
		}
		this.redstone = redstone;
	}
	
	@Override
	public void onRedstoneState(boolean powered) {
		if(redstone){
			System.err.println("Enabled: " + powered);
			this.enabled = powered;
			updateShields(hasIons(), true);
		}
	}
}