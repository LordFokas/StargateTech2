package stargatetech2.common.packet;

import net.minecraft.entity.player.EntityPlayer;
import stargatetech2.StargateTech2;
import stargatetech2.common.base.BasePacket.ClientToServer;
import cpw.mods.fml.relauncher.Side;

@ClientToServer
public class PacketOpenGUI extends PacketCoordinates {
	public int guiID;

	@Override
	protected void writeData() throws Exception {
		output.writeInt(guiID);
	}

	@Override
	protected void readData(EntityPlayer player, Side side) throws Exception {
		guiID = input.readInt();
		player.openGui(StargateTech2.instance, guiID, player.worldObj, x, y, z);
	}
}