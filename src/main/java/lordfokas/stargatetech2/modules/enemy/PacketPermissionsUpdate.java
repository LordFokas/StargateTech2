package lordfokas.stargatetech2.modules.enemy;

import lordfokas.stargatetech2.lib.packet.BasePacket.ClientToServer;
import lordfokas.stargatetech2.lib.packet.PacketCoordinates;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.relauncher.Side;

@ClientToServer
public class PacketPermissionsUpdate extends PacketCoordinates {
	public boolean isSetting;
	public int permissionFlag;
	
	@Override
	protected void writeData() throws Exception {
		output.writeBoolean(isSetting);
		output.writeInt(permissionFlag);
	}
	
	@Override
	protected IMessage readData(EntityPlayer player, Side side) throws Exception {
		isSetting = input.readBoolean();
		permissionFlag = input.readInt();
		TileShieldController controller = (TileShieldController) player.worldObj.getTileEntity(x, y, z);
		controller.getServerContext().updatePermissions(isSetting, permissionFlag);
		return null;
	}
}