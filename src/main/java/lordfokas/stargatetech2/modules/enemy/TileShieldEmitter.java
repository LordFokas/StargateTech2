package lordfokas.stargatetech2.modules.enemy;

import java.util.LinkedList;

import lordfokas.stargatetech2.api.shields.IShieldable;
import lordfokas.stargatetech2.modules.ModuleEnemy;
import lordfokas.stargatetech2.modules.core.machine.FaceColor;
import lordfokas.stargatetech2.modules.core.machine.TileMachine;
import lordfokas.stargatetech2.util.ConfigServer;
import lordfokas.stargatetech2.util.Vec3Int;
import lordfokas.stargatetech2.util.api.WeakBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileShieldEmitter extends TileMachine implements IShieldControllerProvider{
	private Vec3Int controller;
	
	public void setController(Vec3Int controller){
		this.controller = controller;
		TileEntity te = worldObj.getTileEntity(controller.x, controller.y, controller.z);
		if(te instanceof TileShieldController){
			((TileShieldController)te).addEmitter(this);
		}
	}
	
	@Override
	public void invalidate(){
		super.invalidate();
		if(controller == null) return;
		TileEntity te = worldObj.getTileEntity(controller.x, controller.y, controller.z);
		if(te instanceof TileShieldController){
			((TileShieldController)te).removeEmitter(this);
		}
	}
	
	public LinkedList<Vec3Int> createShields(){
		ForgeDirection fd = ForgeDirection.values()[getBlockMetadata()];
		LinkedList<Vec3Int> list = new LinkedList();
		Vec3Int pos = new Vec3Int(xCoord, yCoord, zCoord);
		for(int i = 0; i < ConfigServer.shieldEmitterRange; i++){
			pos = pos.offset(fd);
			Block b = worldObj.getBlock(pos.x, pos.y, pos.z);
			if(b.isAir(worldObj, pos.x, pos.y, pos.z) || b == null || WeakBlockRegistry.isRemovable(b, worldObj.getBlockMetadata(pos.x, pos.y, pos.z))){
				worldObj.setBlock(pos.x, pos.y, pos.z, ModuleEnemy.shield, 0, 2);
				((TileShield)worldObj.getTileEntity(pos.x, pos.y, pos.z)).setController(controller);
				list.add(pos);
			}else if(b instanceof IShieldable){
				((IShieldable)b).onShield(worldObj, pos.x, pos.y, pos.z, controller.x, controller.y, controller.z);
				list.add(pos);
			}else break;
		}
		return list;
	}
	
	@Override public boolean canUpdate(){ return false; }
	
	@Override // Never used;
	protected FaceColor[] getPossibleFaceColors(){ return null; }

	@Override
	protected void readNBT(NBTTagCompound nbt){
		controller = Vec3Int.fromNBT(nbt.getCompoundTag("controller"));
	}

	@Override
	protected void writeNBT(NBTTagCompound nbt){
		nbt.setTag("controller", controller.toNBT());
	}
	
	@Override
	public Vec3Int getShieldControllerCoords(){
		return controller;
	}
}