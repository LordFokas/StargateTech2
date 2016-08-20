package lordfokas.naquadria.tileentity;

import lordfokas.naquadria.gui.BaseContainer;
import lordfokas.naquadria.tileentity.ITileContext.Client;
import lordfokas.naquadria.tileentity.ITileContext.Server;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

/**
 * A basic TileEntity. It splits the logic into Client / Server context, which can
 * inherit from a common context. Refer to {@link ITileContext} for which interfaces
 * each context should implement.
 * 
 * Also supplies basic automated GUI Sync if used along with a {@link BaseContainer}.
 * Refer to {@link ISyncedGUI} for the interfaces each context should implement in
 * order to be automatically synced.
 *
 * @param <C> The type {@link BaseTileEntity#getClientContext()} should return.
 * @param <S> The type {@link BaseTileEntity#getServerContext()} should return.
 * 
 * @author LordFokas
 */
public class BaseTileEntity<C extends Client, S extends Server> extends TileEntity
implements ITickable, ITile.Client, ITile.Server, ISyncedGUI.Flow{
	protected final ITileContext context;
	protected Side side;
	
	protected BaseTileEntity(Class<? extends C> client, Class<? extends S> server){
		side = FMLCommonHandler.instance().getEffectiveSide();
		ITileContext context = null;
		try{
			if(side.isClient() && client != null){
				context = client.newInstance();
				context.setTile(this);
				((ITileContext.Client)context).setTile(this);
			}else if(side.isServer() && server != null){
				context = server.newInstance();
				context.setTile(this);
				((ITileContext.Server)context).setTile(this);
			}
		}catch(Exception e){
			FMLCommonHandler.instance().raiseException(e, "Invalid BaseTileEntity construction", true);
		}
		this.context = context;
	}
	
	@Override
	public final void update(){
		context.tick();
	}
	
	/*@Override // TODO Optimize this
	public final boolean canUpdate(){
		if(context == null) return false;
		return context.canTick();
	}*/
	
	@Override
	public final SPacketUpdateTileEntity getUpdatePacket(){
        NBTTagCompound nbt = new NBTTagCompound();
        this.writeToNBT(nbt);
        return new SPacketUpdateTileEntity(pos, 1, nbt);
    }
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt){
		NBTTagCompound nbt = pkt.getNbtCompound();
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
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		if(context instanceof ITileContext.Server){
			NBTTagCompound contextNBT = new NBTTagCompound();
			((ITileContext.Server)context).writeNBTData(contextNBT);
			nbt.setTag("context", contextNBT);
		}
		return nbt;
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
		if(side.isClient()) return;
		// TODO: check for consistency and clean up
		// worldObj.markBlockForUpdate(pos);
		worldObj.notifyBlockOfStateChange(pos, blockType);
	}
	
	@Override
	public World getWorld() {
		return worldObj;
	}

	@Override
	public BlockPos pos() {
		return pos;
	}
	
	
	// ##################################################################
	// ISyncedGUI
	
	@Override
	public int[] getKeyArray(){
		if(context instanceof ISyncedGUI.Source){
			return ((ISyncedGUI.Source)context).getKeyArray();
		} else return new int[]{};
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