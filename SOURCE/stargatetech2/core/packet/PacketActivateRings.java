package stargatetech2.core.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.relauncher.Side;
import stargatetech2.common.base.BasePacket;
import stargatetech2.core.tileentity.TileTransportRing;

public class PacketActivateRings extends BasePacket {
	public int x;
	public int y;
	public int z;
	public boolean up;
	
	@Override
	protected void onBeforeSend() throws Exception{
		output.writeInt(x);
		output.writeInt(y);
		output.writeInt(z);
		output.writeBoolean(up);
	}

	@Override
	public void onReceive(EntityPlayer player, Side side) throws Exception {
		x = input.readInt();
		y = input.readInt();
		z = input.readInt();
		up = input.readBoolean();
		TileEntity te = player.worldObj.getBlockTileEntity(x, y, z);
		if(te instanceof TileTransportRing){
			((TileTransportRing)te).teleport(up);
		}
	}
}