package lordfokas.stargatetech2.core.packet;

import lordfokas.stargatetech2.core.base.BasePacket;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.relauncher.Side;

public abstract class PacketCoordinates extends BasePacket<PacketCoordinates, IMessage> {
	public int x, y, z;
	
	@Override
	protected final void serialize() throws Exception {
		output.writeInt(x);
		output.writeInt(y);
		output.writeInt(z);
		writeData();
	}
	
	@Override
	public final IMessage unserialize(EntityPlayer player, Side side) throws Exception {
		x = input.readInt();
		y = input.readInt();
		z = input.readInt();
		return readData(player, side);
	}
	
	protected abstract void writeData() throws Exception;
	protected abstract IMessage readData(EntityPlayer player, Side side) throws Exception;
}
