package stargatetech2.transport.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import stargatetech2.core.base.BasePacket.ClientToServer;
import stargatetech2.core.packet.PacketCoordinates;
import stargatetech2.transport.tileentity.TileTransportRing;
import cpw.mods.fml.relauncher.Side;

@ClientToServer
public class PacketActivateRings extends PacketCoordinates {
	public boolean up;

	@Override
	protected void writeData() throws Exception {
		output.writeBoolean(up);
	}

	@Override
	protected void readData(EntityPlayer player, Side side) throws Exception {
		up = input.readBoolean();
		TileEntity te = player.worldObj.getBlockTileEntity(x, y, z);
		if(te instanceof TileTransportRing){
			((TileTransportRing)te).teleport(up, 1);
		}
	}
}