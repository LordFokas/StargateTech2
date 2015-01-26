package lordfokas.stargatetech2.modules.automation;

import lordfokas.stargatetech2.lib.packet.BasePacket.ClientToServer;
import lordfokas.stargatetech2.lib.packet.PacketCoordinates;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.relauncher.Side;

@ClientToServer
public class PacketUpdateBusEnabled extends PacketCoordinates {
	public boolean enabled;
	
	@Override
	protected void writeData() throws Exception {
		output.writeBoolean(enabled);
	}

	@Override
	protected IMessage readData(EntityPlayer player, Side side) throws Exception {
		enabled = input.readBoolean();
		TileEntity te = player.worldObj.getTileEntity(x, y, z);
		if(te instanceof ISyncBusDevice){
			((ISyncBusDevice)te).setEnabled(enabled);
		}
		return null;
	}
}
