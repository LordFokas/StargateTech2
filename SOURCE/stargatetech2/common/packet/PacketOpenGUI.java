package stargatetech2.common.packet;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.Side;
import stargatetech2.StargateTech2;
import stargatetech2.common.base.BasePacket;

public class PacketOpenGUI extends BasePacket {
	public int guiID;
	public int x, y, z;
	
	@Override
	protected void onBeforeSend() throws Exception {
		output.writeInt(guiID);
		output.writeInt(x);
		output.writeInt(y);
		output.writeInt(z);
	}
	
	@Override
	public void onReceive(EntityPlayer player, Side side) throws Exception {
		guiID = input.readInt();
		x = input.readInt();
		y = input.readInt();
		z = input.readInt();
		player.openGui(StargateTech2.instance, guiID, player.worldObj, x, y, z);
	}
}