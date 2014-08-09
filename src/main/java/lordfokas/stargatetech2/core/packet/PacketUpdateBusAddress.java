package lordfokas.stargatetech2.core.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import lordfokas.stargatetech2.core.base.BasePacket.ClientToServer;
import lordfokas.stargatetech2.core.machine.tabs.TabAbstractBus.ISyncBusDevice;
import cpw.mods.fml.relauncher.Side;

@ClientToServer
public class PacketUpdateBusAddress extends PacketCoordinates {
	public short address;
	
	@Override
	protected void writeData() throws Exception {
		output.writeShort(address);
	}

	@Override
	protected void readData(EntityPlayer player, Side side) throws Exception {
		address = input.readShort();
		TileEntity te = player.worldObj.getTileEntity(x, y, z);
		if(te instanceof ISyncBusDevice){
			((ISyncBusDevice)te).setAddress(address);
		}
	}

}
