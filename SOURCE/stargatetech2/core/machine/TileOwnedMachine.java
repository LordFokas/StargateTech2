package stargatetech2.core.machine;

import net.minecraft.nbt.NBTTagCompound;
import stargatetech2.integration.plugins.te3.CoFHFriendHelper;

public abstract class TileOwnedMachine extends TileMachine implements IOwnedMachine{
	public enum AccessMode{
		PRIVATE, FRIENDS, PUBLIC
	}
	
	private AccessMode mode = AccessMode.PRIVATE;
	private String owner = null;
	
	public final AccessMode getAccessMode(){
		return mode;
	}
	
	public final void setAccessMode(AccessMode mode){
		this.mode = mode;
	}
	
	@Override
	public final String getOwner() {
		return owner;
	}

	@Override
	public final void setOwner(String owner) {
		if(this.owner != null)
			this.owner = owner;
	}

	@Override
	public boolean hasAccess(String player) {
		if(owner != null){
			switch(mode){
				case FRIENDS:
					if(CoFHFriendHelper.isSystemEnabled()){
						return player.equalsIgnoreCase(owner) || CoFHFriendHelper.isFriend(player, owner);
					}
				case PRIVATE:
					return player.equalsIgnoreCase(owner);
				case PUBLIC:
					return true;
			}
		}
		return true;
	}
	
	protected final void writeOwnedMachineData(NBTTagCompound nbt){
		nbt.setString("__owner", owner);
		nbt.setInteger("__mode", mode.ordinal());
	}
	
	protected final void readOwnedMachineData(NBTTagCompound nbt){
		owner = nbt.getString("__owner");
		mode = AccessMode.values()[nbt.getInteger("__mode")];
	}
}
