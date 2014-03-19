package stargatetech2.enemy.packet;

import net.minecraft.entity.player.EntityPlayer;
import stargatetech2.core.base.BasePacket;
import stargatetech2.core.base.BasePacket.ClientToServer;
import stargatetech2.enemy.tileentity.TileShieldController;
import cpw.mods.fml.relauncher.Side;

@ClientToServer
public class PacketPermissionsUpdate extends BasePacket {
	public TileShieldController controller;
	public boolean isSetting;
	public int permissionFlag;
	
	@Override
	protected void onBeforeSend() throws Exception {
		output.writeInt(controller.xCoord);
		output.writeInt(controller.yCoord);
		output.writeInt(controller.zCoord);
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
		controller = (TileShieldController) player.worldObj.getBlockTileEntity(x, y, z);
		controller.updatePermissions(isSetting, permissionFlag);
	}
}