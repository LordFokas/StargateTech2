package lordfokas.naquadria.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

public abstract class PacketCoordinates extends BasePacket<PacketCoordinates, IMessage> {
	public BlockPos coordinates;
	
	@Override
	protected final void serialize() throws Exception {
		output.writeInt(coordinates.getX());
		output.writeInt(coordinates.getY());
		output.writeInt(coordinates.getZ());
		writeData();
	}
	
	@Override
	public final IMessage unserialize(EntityPlayer player, Side side) throws Exception {
		int x = input.readInt();
		int y = input.readInt();
		int z = input.readInt();
		coordinates = new BlockPos(x, y, z);
		return readData(player, side);
	}
	
	protected abstract void writeData() throws Exception;
	protected abstract IMessage readData(EntityPlayer player, Side side) throws Exception;
}
