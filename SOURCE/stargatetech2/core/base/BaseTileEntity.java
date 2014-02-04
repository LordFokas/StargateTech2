package stargatetech2.core.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.network.PacketDispatcher;

public abstract class BaseTileEntity extends TileEntity {
	
	/**
	 * Marks things that belong to the server logic
	 * and aren't used anywhere else.
	 * 
	 * @author LordFokas
	 */
	@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
	@Retention(RetentionPolicy.SOURCE)
	public @interface ServerLogic{}
	
	/**
	 * Marks things that belong to the client logic
	 * and aren't used anywhere else.
	 * 
	 * @author LordFokas
	 */
	@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
	@Retention(RetentionPolicy.SOURCE)
	public @interface ClientLogic{}
	
	public final Packet132TileEntityData getDescriptionPacket(){
        NBTTagCompound nbt = new NBTTagCompound();
        this.writeToNBT(nbt);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, nbt);
    }
	
	public final void onDataPacket(INetworkManager net, Packet132TileEntityData packet){
		NBTTagCompound nbt = packet.data;
		if(nbt != null){
			this.readFromNBT(nbt);
		}
    }
	
	public final void updateClients(){
		if(worldObj.isRemote) return;
		Packet132TileEntityData packet = this.getDescriptionPacket();
		PacketDispatcher.sendPacketToAllInDimension(packet, this.worldObj.provider.dimensionId);
	}
	
	@Override
	public final void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		readNBT(nbt);
	}
	
	@Override
	public final void writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		writeNBT(nbt);
	}
	
	protected abstract void readNBT(NBTTagCompound nbt);
	protected abstract void writeNBT(NBTTagCompound nbt);
}