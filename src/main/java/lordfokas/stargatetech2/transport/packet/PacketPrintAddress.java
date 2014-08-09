package lordfokas.stargatetech2.transport.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import lordfokas.stargatetech2.api.stargate.Address;
import lordfokas.stargatetech2.api.stargate.ITileStargate;
import lordfokas.stargatetech2.core.base.BasePacket.ClientToServer;
import lordfokas.stargatetech2.core.packet.PacketCoordinates;
import cpw.mods.fml.relauncher.Side;

@ClientToServer
public class PacketPrintAddress extends PacketCoordinates {

	@Override
	protected void writeData() throws Exception {}

	@Override
	protected void readData(EntityPlayer player, Side side) throws Exception {
		TileEntity te = player.worldObj.getTileEntity(x, y, z);
		if(te instanceof ITileStargate){
			Address address = ((ITileStargate)te).getAddress();
			player.sendChatToPlayer(new ChatMessageComponent().addText("This Stargate uses the address: " +
					EnumChatFormatting.GOLD + address.toString()));
		}
	}
}