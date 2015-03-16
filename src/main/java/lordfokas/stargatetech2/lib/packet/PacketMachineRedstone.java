package lordfokas.stargatetech2.lib.packet;

import lordfokas.stargatetech2.lib.packet.BasePacket.SidesReceivedOn;
import lordfokas.stargatetech2.lib.tileentity.TileEntityMachine;
import lordfokas.stargatetech2.lib.util.TileEntityHelper;
import net.minecraft.entity.player.EntityPlayer;
import cofh.api.tileentity.IRedstoneControl.ControlMode;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.relauncher.Side;

@SidesReceivedOn(Side.SERVER)
public class PacketMachineRedstone extends PacketCoordinates{
	public ControlMode mode;
	
	@Override
	protected void writeData() throws Exception {
		output.writeInt(mode.ordinal());
	}

	@Override
	protected IMessage readData(EntityPlayer player, Side side) throws Exception {
		mode = ControlMode.values()[input.readInt()];
		TileEntityMachine machine = TileEntityHelper.getTileEntityAs(player.worldObj, x, y, z, TileEntityMachine.class);
		machine.setControl(mode);
		return null;
	}
	
}
