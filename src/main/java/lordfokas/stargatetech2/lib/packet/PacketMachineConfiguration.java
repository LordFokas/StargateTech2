package lordfokas.stargatetech2.lib.packet;

import lordfokas.stargatetech2.lib.packet.BasePacket.SidesReceivedOn;
import lordfokas.stargatetech2.lib.tileentity.TileEntityMachine;
import lordfokas.stargatetech2.lib.util.TileEntityHelper;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.relauncher.Side;

@SidesReceivedOn(Side.SERVER)
public class PacketMachineConfiguration extends PacketCoordinates{
	public boolean increase;
	public boolean reset;
	public int side;
	
	@Override
	protected void writeData() throws Exception {
		output.writeBoolean(increase);
		output.writeBoolean(reset);
		output.writeInt(side);
	}

	@Override
	protected IMessage readData(EntityPlayer player, Side s) throws Exception {
		increase = input.readBoolean();
		reset = input.readBoolean();
		side = input.readInt();
		TileEntityMachine machine = TileEntityHelper.getTileEntityAs(player.worldObj, x, y, z, TileEntityMachine.class);
		if(reset){
			machine.resetSides();
		}else if(increase){
			machine.incrSide(side);
		}else{
			machine.decrSide(side);
		}
		return null;
	}
	
}
