package lordfokas.stargatetech2.enemy.packet;

import net.minecraft.entity.player.EntityPlayer;
import lordfokas.stargatetech2.core.base.BasePacket.ClientToServer;
import lordfokas.stargatetech2.core.packet.PacketCoordinates;
import lordfokas.stargatetech2.enemy.tileentity.TileShieldController;
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
	protected void readData(EntityPlayer player, Side side) throws Exception {
		isSetting = input.readBoolean();
		permissionFlag = input.readInt();
		TileShieldController controller = (TileShieldController) player.worldObj.getBlockTileEntity(x, y, z);
		controller.updatePermissions(isSetting, permissionFlag);
	}
}