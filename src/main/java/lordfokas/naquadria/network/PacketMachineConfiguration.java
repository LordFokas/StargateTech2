package lordfokas.naquadria.network;

import lordfokas.naquadria.network.BasePacket.SidesReceivedOn;
import lordfokas.naquadria.tileentity.TileEntityHelper;
import lordfokas.naquadria.tileentity.TileEntityMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

@SidesReceivedOn(Side.SERVER)
public class PacketMachineConfiguration extends PacketCoordinates{
	public boolean increase;
	public boolean reset;
	public EnumFacing side;
	
	@Override
	protected void writeData() throws Exception {
		output.writeBoolean(increase);
		output.writeBoolean(reset);
		output.writeInt(side.ordinal());
	}

	@Override
	protected IMessage readData(EntityPlayer player, Side s) throws Exception {
		increase = input.readBoolean();
		reset = input.readBoolean();
		side = EnumFacing.values()[input.readInt()];
		TileEntityMachine machine = TileEntityHelper.getTileEntityAs(player.worldObj, coordinates, TileEntityMachine.class);
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
