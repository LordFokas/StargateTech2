package stargatetech2.common.packet;

import net.minecraft.entity.player.EntityPlayer;
import stargatetech2.common.base.BasePacket;
import cpw.mods.fml.relauncher.Side;

public abstract class PacketCoordinates extends BasePacket {
	public int x, y, z;
	
	@Override
	protected final void onBeforeSend() throws Exception {
		output.writeInt(x);
		output.writeInt(y);
		output.writeInt(z);
		writeData();
	}
	
	@Override
	public final void onReceive(EntityPlayer player, Side side) throws Exception {
		x = input.readInt();
		y = input.readInt();
		z = input.readInt();
		readData(player, side);
	}
	
	protected abstract void writeData() throws Exception;
	protected abstract void readData(EntityPlayer player, Side side) throws Exception;
}
