package lordfokas.stargatetech2.core.packet;

import lordfokas.stargatetech2.core.base.BasePacket;
import lordfokas.stargatetech2.core.base.BasePacket.ClientToServer;
import lordfokas.stargatetech2.core.machine.tabs.TabAbstractBus.ISyncBusDevice;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.relauncher.Side;

@ClientToServer
public class PacketUpdateBusAddress extends PacketCoordinates {
	public short address;
	
	@Override
	protected void writeData() throws Exception {
		output.writeShort(address);
	}

	@Override
	protected BasePacket readData(EntityPlayerMP player, Side side) throws Exception {
		address = input.readShort();
		TileEntity te = player.worldObj.getTileEntity(x, y, z);
		if(te instanceof ISyncBusDevice){
			((ISyncBusDevice)te).setAddress(address);
		}
		return null;
	}

}