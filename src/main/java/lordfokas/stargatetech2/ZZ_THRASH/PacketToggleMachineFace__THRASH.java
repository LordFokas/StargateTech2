package lordfokas.stargatetech2.ZZ_THRASH;

import lordfokas.stargatetech2.lib.packet.BasePacket.ClientToServer;

@Deprecated
@ClientToServer
public class PacketToggleMachineFace__THRASH /* extends PacketCoordinates */{
	/*public Face face;
	
	@Override
	protected void writeData() throws Exception {
		output.writeInt(face.ordinal());
	}

	@Override
	protected IMessage readData(EntityPlayer player, Side side) throws Exception {
		face = Face.values()[input.readInt()];
		TileEntity te = player.worldObj.getTileEntity(x, y, z);
		if(te instanceof TileMachine__THRASH){
			((TileMachine__THRASH)te).toggleFace(face);
		}
		return null;
	}*/
}