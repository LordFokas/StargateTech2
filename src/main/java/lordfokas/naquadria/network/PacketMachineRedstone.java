package lordfokas.naquadria.network;

import cofh.api.tileentity.IRedstoneControl.ControlMode;
import lordfokas.naquadria.network.BasePacket.SidesReceivedOn;
import lordfokas.naquadria.tileentity.TileEntityHelper;
import lordfokas.naquadria.tileentity.TileEntityMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

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
		TileEntityMachine machine = TileEntityHelper.getTileEntityAs(player.worldObj, coordinates, TileEntityMachine.class);
		machine.setControl(mode);
		return null;
	}
	
}
