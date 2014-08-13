package lordfokas.stargatetech2.core.packet;

import lordfokas.stargatetech2.core.base.BasePacket;
import lordfokas.stargatetech2.core.base.BasePacket.ClientToServer;
import lordfokas.stargatetech2.core.machine.tabs.TabAbstractBus.ISyncBusDevice;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.relauncher.Side;

@ClientToServer
public class PacketUpdateBusEnabled extends PacketCoordinates {
	public boolean enabled;
	
	@Override
	protected void writeData() throws Exception {
		output.writeBoolean(enabled);
	}

	@Override
	protected BasePacket readData(EntityPlayerMP player, Side side) throws Exception {
		enabled = input.readBoolean();
		TileEntity te = player.worldObj.getTileEntity(x, y, z);
		if(te instanceof ISyncBusDevice){
			((ISyncBusDevice)te).setEnabled(enabled);
		}
		return null;
	}
}
