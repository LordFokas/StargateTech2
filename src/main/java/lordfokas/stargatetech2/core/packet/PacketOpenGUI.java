package lordfokas.stargatetech2.core.packet;

import lordfokas.stargatetech2.StargateTech2;
import lordfokas.stargatetech2.core.base.BasePacket;
import lordfokas.stargatetech2.core.base.BasePacket.ClientToServer;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.relauncher.Side;

@ClientToServer
public class PacketOpenGUI extends PacketCoordinates {
	public int guiID;

	@Override
	protected void writeData() throws Exception {
		output.writeInt(guiID);
	}

	@Override
	protected BasePacket readData(EntityPlayerMP player, Side side) throws Exception {
		guiID = input.readInt();
		player.openGui(StargateTech2.instance, guiID, player.worldObj, x, y, z);
		return null;
	}
}