package lordfokas.stargatetech2.lib.tileentity;

import lordfokas.stargatetech2.lib.tileentity.ITileContext.Client;
import lordfokas.stargatetech2.lib.tileentity.ITileContext.Server;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class BaseTileEntity<C extends Client, S extends Server> extends TileEntity implements ITile.Client, ITile.Server, ISyncedGUI.Flow{
	private ITileContext context = null;
	
	public BaseTileEntity(Class<? extends C> client, Class<? extends S> server){
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		try{
			if(side == Side.CLIENT && client != null){
				context = client.newInstance();
				((ITileContext.Client)context).setTile(this);
			}else if(side == Side.SERVER && server != null){
				context = server.newInstance();
				((ITileContext.Server)context).setTile(this);
			}
		}catch(Exception e){
			FMLCommonHandler.instance().raiseException(e, "Invalid BaseTileEntity construction", true);
		}
	}
	
	@Override
	public final void updateEntity(){
		context.tick();
	}
	
	@Override
	public final boolean canUpdate(){
		if(context == null) return false;
		return context.canTick();
	}
	
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
	
	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		if(context != null){
			NBTTagCompound contextNBT = nbt.getCompoundTag("context");
			if(contextNBT != null){
				context.readNBTData(contextNBT);
			}
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		if(context instanceof ITileContext.Server){
			NBTTagCompound contextNBT = new NBTTagCompound();
			((ITileContext.Server)context).writeNBTData(contextNBT);
			nbt.setTag("context", contextNBT);
		}
	}
	
	public final ITileContext getContext(){
		return context;
	}
	
	public final C getClientContext(){
		if(context instanceof ITileContext.Client){
			return (C) context;
		}
		return null;
	}
	
	public final S getServerContext(){
		if(context instanceof ITileContext.Server){
			return (S) context;
		}
		return null;
	}
	
	
	// ##################################################################
	// ITile Provider
	
	@Override
	public final void updateClients(){
		if(worldObj.isRemote) return;
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	@Override
	public World getWorld() {
		return worldObj;
	}

	@Override
	public int x() {
		return xCoord;
	}

	@Override
	public int y() {
		return yCoord;
	}

	@Override
	public int z() {
		return zCoord;
	}
	
	
	// ##################################################################
	// ISyncedGUI
	
	@Override
	public int getValueCount(){
		if(context instanceof ISyncedGUI.Source){
			return ((ISyncedGUI.Source)context).getValueCount();
		} else return 0;
	}

	@Override
	public int getValue(int key) {
		return ((ISyncedGUI.Source)context).getValue(key);
	}

	@Override
	public void setValue(int key, int val) {
		if(context instanceof ISyncedGUI.Sink){
			((ISyncedGUI.Sink)context).setValue(key, val);
		}
	}
}