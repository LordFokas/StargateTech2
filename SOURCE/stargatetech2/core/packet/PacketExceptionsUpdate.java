package stargatetech2.core.packet;

import net.minecraft.entity.player.EntityPlayer;
import stargatetech2.common.base.BasePacket;
import stargatetech2.common.base.BasePacket.ClientToServer;
import stargatetech2.core.tileentity.TileShieldEmitter;
import cpw.mods.fml.relauncher.Side;

@ClientToServer
public class PacketExceptionsUpdate extends BasePacket {
	public TileShieldEmitter emitter;
	public boolean isSetting;
	public String playerName;
	
	@Override
	protected void onBeforeSend() throws Exception {
		output.writeInt(emitter.xCoord);
		output.writeInt(emitter.yCoord);
		output.writeInt(emitter.zCoord);
		output.writeBoolean(isSetting);
		output.writeUTF(playerName);
	}

	@Override
	public void onReceive(EntityPlayer player, Side side) throws Exception {
		int x = input.readInt();
		int y = input.readInt();
		int z = input.readInt();
		isSetting = input.readBoolean();
		playerName = input.readUTF();
		emitter = (TileShieldEmitter) player.worldObj.getBlockTileEntity(x, y, z);
		emitter.updateExceptions(isSetting, playerName);
	}
}