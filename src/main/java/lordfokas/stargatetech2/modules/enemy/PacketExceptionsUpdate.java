package lordfokas.stargatetech2.modules.enemy;

import lordfokas.stargatetech2.lib.packet.BasePacket.ClientToServer;
import lordfokas.stargatetech2.lib.packet.PacketCoordinates;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.relauncher.Side;

@ClientToServer
public class PacketExceptionsUpdate extends PacketCoordinates {
	public boolean isSetting;
	public String playerName;
	
	@Override
	protected void writeData() throws Exception {
		output.writeBoolean(isSetting);
		writeUTF(playerName);
	}

	public void writeUTF(final String name){
		byte[] temp = name.getBytes();
		output.writeInt(temp.length);
		output.writeBytes(temp);
	}
	
	public String readUTF(){
		int size=input.readInt();
		byte[] nameByte= new byte[size];
		input.readBytes(nameByte);
		return new String(nameByte);
	}
	
	@Override
	protected IMessage readData(EntityPlayer player, Side side) throws Exception {
		isSetting = input.readBoolean();
		playerName = readUTF();
		TileShieldController controller = (TileShieldController) player.worldObj.getTileEntity(x, y, z);
		controller.getServerContext().updateExceptions(isSetting, playerName);
		return null;
	}
}