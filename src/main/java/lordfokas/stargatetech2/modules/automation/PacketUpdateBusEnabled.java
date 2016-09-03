package lordfokas.stargatetech2.modules.automation;

import lordfokas.naquadria.network.BasePacket.ClientToServer;
import lordfokas.naquadria.network.PacketCoordinates;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

@ClientToServer
public class PacketUpdateBusEnabled extends PacketCoordinates {
	public boolean enabled;
	
	@Override
	protected void writeData() throws Exception {
		output.writeBoolean(enabled);
	}

	@Override
	protected IMessage readData(EntityPlayer player, Side side) throws Exception {
		enabled = input.readBoolean();
		TileEntity te = player.worldObj.getTileEntity(coordinates);
		if(te instanceof ISyncBusDevice){
			((ISyncBusDevice)te).setEnabled(enabled);
		}
		return null;
	}
}
