package lordfokas.stargatetech2.modules.core.packets__THRASH;

import lordfokas.stargatetech2.lib.packet.BasePacket.ClientToServer;
import lordfokas.stargatetech2.lib.packet.PacketCoordinates;
import lordfokas.stargatetech2.modules.core.machine__TRASH.Face__THRASH;
import lordfokas.stargatetech2.modules.core.machine__TRASH.TileMachine__THRASH;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.relauncher.Side;

@ClientToServer
public class PacketToggleMachineFace__THRASH extends PacketCoordinates {
	public Face__THRASH face;
	
	@Override
	protected void writeData() throws Exception {
		output.writeInt(face.ordinal());
	}

	@Override
	protected IMessage readData(EntityPlayer player, Side side) throws Exception {
		face = Face__THRASH.values()[input.readInt()];
		TileEntity te = player.worldObj.getTileEntity(x, y, z);
		if(te instanceof TileMachine__THRASH){
			((TileMachine__THRASH)te).toggleFace(face);
		}
		return null;
	}
}