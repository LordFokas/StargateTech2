package lordfokas.stargatetech2.transport.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import lordfokas.stargatetech2.core.base.BasePacket.ClientToServer;
import lordfokas.stargatetech2.core.base.BasePacket.ServerToClient;
import lordfokas.stargatetech2.core.packet.PacketCoordinates;
import lordfokas.stargatetech2.transport.tileentity.TileStargate;
import cpw.mods.fml.relauncher.Side;

@ClientToServer
@ServerToClient
public class PacketWormhole extends PacketCoordinates {
	public static final byte SYNC_REQUEST	= 0x01;
	public static final byte SYNC_INACTIVE	= 0x02;
	public static final byte SYNC_ACTIVE	= 0x03;
	
	private byte type = SYNC_REQUEST;
	
	public static PacketWormhole syncRequest(int x, int y, int z){
		PacketWormhole packet = new PacketWormhole();
		packet.x = x;
		packet.y = y;
		packet.z = z;
		return packet;
	}
	
	public static PacketWormhole sendSync(int x, int y, int z, boolean status){
		PacketWormhole packet = new PacketWormhole();
		packet.type = status ? SYNC_ACTIVE : SYNC_INACTIVE;
		packet.x = x;
		packet.y = y;
		packet.z = z;
		return packet;
	}
	
	@Override
	protected void writeData() throws Exception {
		output.writeByte(type);
	}

	@Override
	protected void readData(EntityPlayer player, Side side) throws Exception {
		type = input.readByte();
		switch(type){
			case SYNC_REQUEST:
				TileEntity te = player.worldObj.getTileEntity(x, y, z);
				if(te instanceof TileStargate){
					PacketWormhole response = sendSync(x, y, z, ((TileStargate)te).hasActiveWormhole());
					response.sendToPlayer(player);
				}
				break;
			case SYNC_INACTIVE:
			case SYNC_ACTIVE:
				boolean active = (type == SYNC_ACTIVE);
				TileEntity sg = player.worldObj.getTileEntity(x, y, z);
				if(sg instanceof TileStargate){
					((TileStargate)sg).setHasWormhole(active);
				}
				break;
		}
	}
}