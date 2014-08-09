package lordfokas.stargatetech2.core.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import lordfokas.stargatetech2.core.base.BasePacket.ServerToClient;
import lordfokas.stargatetech2.core.machine.FaceColor;
import lordfokas.stargatetech2.core.machine.TileMachine;
import cpw.mods.fml.relauncher.Side;

@ServerToClient
public class PacketUpdateMachineColors extends PacketCoordinates {
	private FaceColor[] colors;
	
	public PacketUpdateMachineColors(){
		colors = new FaceColor[6];
	}
	
	public PacketUpdateMachineColors(FaceColor[] colors){
		this.colors = colors;
	}
	
	@Override
	protected void writeData() throws Exception{
		for(FaceColor color : colors){
			output.writeByte(color.ordinal());
		}
	}

	@Override
	protected void readData(EntityPlayer player, Side side) throws Exception {
		for(int s = 0; s < 6; s++){
			colors[s] = FaceColor.values()[input.readByte()];
		}
		TileEntity te = player.worldObj.getTileEntity(x, y, z);
		if(te instanceof TileMachine){
			((TileMachine)te).updateColors(colors);
		}
	}
}