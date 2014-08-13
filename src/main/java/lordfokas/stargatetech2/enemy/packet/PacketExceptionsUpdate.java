package lordfokas.stargatetech2.enemy.packet;

import lordfokas.stargatetech2.core.base.BasePacket;
import lordfokas.stargatetech2.core.base.BasePacket.ClientToServer;
import lordfokas.stargatetech2.core.packet.PacketCoordinates;
import lordfokas.stargatetech2.enemy.tileentity.TileShieldController;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.relauncher.Side;

@ClientToServer
public class PacketExceptionsUpdate extends PacketCoordinates {
	public boolean isSetting;
	public String playerName;
	
	@Override
	protected void writeData() throws Exception {
		output.writeBoolean(isSetting);
		output.writeUTF(playerName);
	}

	@Override
	protected BasePacket readData(EntityPlayerMP player, Side side) throws Exception {
		isSetting = input.readBoolean();
		playerName = input.readUTF();
		TileShieldController controller = (TileShieldController) player.worldObj.getTileEntity(x, y, z);
		controller.updateExceptions(isSetting, playerName);
		return null;
	}
}