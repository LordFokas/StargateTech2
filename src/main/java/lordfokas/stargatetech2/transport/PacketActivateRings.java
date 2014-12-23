package lordfokas.stargatetech2.transport;

import lordfokas.stargatetech2.core.base.BasePacket.ClientToServer;
import lordfokas.stargatetech2.core.packets.PacketCoordinates;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.relauncher.Side;

@ClientToServer
public class PacketActivateRings extends PacketCoordinates {
	public boolean up;

	@Override
	protected void writeData() throws Exception {
		output.writeBoolean(up);
	}

	@Override
	protected IMessage readData(EntityPlayer player, Side side) throws Exception {
		up = input.readBoolean();
		TileEntity te = player.worldObj.getTileEntity(x, y, z);
		if(te instanceof TileTransportRing){
			((TileTransportRing)te).teleport(up, 1);
		}
		return null;
	}
}