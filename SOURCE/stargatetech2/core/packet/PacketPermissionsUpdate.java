package stargatetech2.core.packet;

import net.minecraft.entity.player.EntityPlayer;
import stargatetech2.common.base.BasePacket;
import stargatetech2.common.base.BasePacket.ClientToServer;
import stargatetech2.core.tileentity.TileShieldEmitter;
import cpw.mods.fml.relauncher.Side;

@ClientToServer
public class PacketPermissionsUpdate extends BasePacket {
	public TileShieldEmitter emitter;
	public boolean isSetting;
	public int permissionFlag;
	
	@Override
	protected void onBeforeSend() throws Exception {
		output.writeInt(emitter.xCoord);
		output.writeInt(emitter.yCoord);
		output.writeInt(emitter.zCoord);
		output.writeBoolean(isSetting);
		output.writeInt(permissionFlag);
	}
	
	@Override
	public void onReceive(EntityPlayer player, Side side) throws Exception {
		int x = input.readInt();
		int y = input.readInt();
		int z = input.readInt();
		isSetting = input.readBoolean();
		permissionFlag = input.readInt();
		emitter = (TileShieldEmitter) player.worldObj.getBlockTileEntity(x, y, z);
		emitter.updatePermissions(isSetting, permissionFlag);
	}
}