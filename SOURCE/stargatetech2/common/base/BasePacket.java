package stargatetech2.common.base;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;
import stargatetech2.common.util.ByteUtil;
import stargatetech2.common.util.PacketHandler;
import stargatetech2.core.packet.PacketExceptionsUpdate;
import stargatetech2.core.packet.PacketPermissionsUpdate;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;

public abstract class BasePacket{
	private static ArrayList<Class<? extends BasePacket>> packetMap = new ArrayList<Class<? extends BasePacket>>();
	
	public static BasePacket fromData(byte[] data){
		int pktID = ByteUtil.readInt(data, 0);
		Class<? extends BasePacket> pktClass = packetMap.get(pktID);
		try {
			return pktClass.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	static{
		packetMap.add(PacketPermissionsUpdate.class);
		packetMap.add(PacketExceptionsUpdate.class);
	}
	
	private ByteArrayOutputStream baos = new ByteArrayOutputStream();
	protected DataOutputStream output = new DataOutputStream(baos);
	protected DataInputStream input;
	
	public final int getPacketID(){
		return packetMap.indexOf(this.getClass());
	}
	
	public final void setData(byte[] bytes) throws Exception{
		input = new DataInputStream(new ByteArrayInputStream(bytes));
		input.readInt(); // skip packet id byte in array.
	}
	
	public final Packet250CustomPayload getPayload(){
		try {
			output.writeInt(getPacketID());
			onBeforeSend();
			return new Packet250CustomPayload(PacketHandler.STARGATE_CHANNEL, baos.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public final void sendToServer(){
		Packet250CustomPayload payload = getPayload();
		if(payload != null) PacketDispatcher.sendPacketToServer(payload);
	}
	
	protected abstract void onBeforeSend() throws Exception;
	public abstract void onReceive(EntityPlayer player, Side side) throws Exception;
}