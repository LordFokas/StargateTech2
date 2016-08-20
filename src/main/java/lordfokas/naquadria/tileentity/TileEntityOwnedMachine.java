package lordfokas.naquadria.tileentity;

import lordfokas.naquadria.tileentity.ITileContext.Client;
import lordfokas.naquadria.tileentity.ITileContext.Server;
import lordfokas.naquadria.tileentity.facing.FaceColor;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityOwnedMachine<C extends Client, S extends Server> extends TileEntityMachine<C, S> implements IOwnedMachine{
	private String owner = "::";
	
	public TileEntityOwnedMachine(Class<? extends C> client, Class<? extends S> server, FaceColor... colors) {
		super(client, server, colors);
	}

	@Override
	public String getOwner() {
		return owner;
	}

	@Override
	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Override
	public boolean hasAccess(String player) {
		return owner.equalsIgnoreCase(player);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		owner = nbt.getString("owner");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setString("owner", owner);
		return nbt;
	}
}