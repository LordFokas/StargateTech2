package lordfokas.stargatetech2.core.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

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
	
	@Override
	public final S35PacketUpdateTileEntity getDescriptionPacket(){
        NBTTagCompound nbt = new NBTTagCompound();
        this.writeToNBT(nbt);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbt);
    }
	
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt){
		NBTTagCompound nbt = pkt.func_148857_g();
		if(nbt != null){
			this.readFromNBT(nbt);
		}
    }
	
	public final void updateClients(){
		if(worldObj.isRemote) return;
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
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