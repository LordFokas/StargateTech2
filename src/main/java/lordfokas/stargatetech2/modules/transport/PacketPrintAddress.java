package lordfokas.stargatetech2.modules.transport;

import lordfokas.naquadria.network.PacketCoordinates;
import lordfokas.naquadria.network.BasePacket.ClientToServer;
import lordfokas.stargatetech2.api.stargate.Address;
import lordfokas.stargatetech2.api.stargate.ITileStargate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.relauncher.Side;

@ClientToServer
public class PacketPrintAddress extends PacketCoordinates {

	@Override
	protected void writeData() throws Exception {}

	@Override
	protected IMessage readData(EntityPlayer player, Side side) throws Exception {
		TileEntity te = player.worldObj.getTileEntity(x, y, z);
		if(te instanceof ITileStargate){
			Address address = ((ITileStargate)te).getAddress();
			player.addChatComponentMessage(new ChatComponentText("This Stargate uses the address: " +
					EnumChatFormatting.GOLD + address.toString()));
		}
		return null;
	}
}