package lordfokas.stargatetech2.modules.automation;

import lordfokas.naquadria.network.BasePacket.ClientToServer;
import lordfokas.naquadria.network.PacketCoordinates;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

@ClientToServer
public class PacketUpdateBusAddress extends PacketCoordinates {
	public short address;
	
	@Override
	protected void writeData() throws Exception {
		output.writeShort(address);
	}

	@Override
	protected IMessage readData(EntityPlayer player, Side side) throws Exception {
		address = input.readShort();
		TileEntity te = player.worldObj.getTileEntity(coordinates);
		if(te instanceof ISyncBusDevice){
			((ISyncBusDevice)te).setAddress(address);
		}
		return null;
	}

}
