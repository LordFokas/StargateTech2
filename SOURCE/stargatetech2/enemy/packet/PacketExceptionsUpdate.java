package stargatetech2.enemy.packet;

import net.minecraft.entity.player.EntityPlayer;
import stargatetech2.core.base.BasePacket.ClientToServer;
import stargatetech2.core.packet.PacketCoordinates;
import stargatetech2.enemy.tileentity.TileShieldController;
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
	protected void readData(EntityPlayer player, Side side) throws Exception {
		isSetting = input.readBoolean();
		playerName = input.readUTF();
		TileShieldController controller = (TileShieldController) player.worldObj.getBlockTileEntity(x, y, z);
		controller.updateExceptions(isSetting, playerName);
	}
}