package lordfokas.stargatetech2.ZZ_THRASH;

import lordfokas.stargatetech2.lib.packet.BasePacket.ClientToServer;
import lordfokas.stargatetech2.lib.packet.PacketCoordinates;
import lordfokas.stargatetech2.lib.tileentity.faces.Face;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.relauncher.Side;

@ClientToServer
public class PacketToggleMachineFace__THRASH extends PacketCoordinates {
	public Face face;
	
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
	}
}