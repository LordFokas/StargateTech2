package stargatetech2.common.base;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;
import stargatetech2.common.packet.PacketOpenGUI;
import stargatetech2.common.util.ByteUtil;
import stargatetech2.common.util.PacketHandler;
import stargatetech2.core.packet.PacketActivateRings;
import stargatetech2.core.packet.PacketExceptionsUpdate;
import stargatetech2.core.packet.PacketPermissionsUpdate;
import stargatetech2.core.packet.PacketWormhole;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public abstract class BasePacket{
	/**
	 * Marks packets the server sends to the clients.
	 * 
	 * @author LordFokas
	 */
	@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
	@Retention(RetentionPolicy.SOURCE)
	public @interface ServerToClient{}
	
	/**
	 * Marks packets the client sends to the server.
	 * 
	 * @author LordFokas
	 */
	@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
	@Retention(RetentionPolicy.SOURCE)
	public @interface ClientToServer{}
	
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
		packetMap.add(PacketOpenGUI.class);
		packetMap.add(PacketActivateRings.class);
		packetMap.add(PacketWormhole.class);
	}
	
	private ByteArrayOutputStream baos = new ByteArrayOutputStream();
	protected DataOutputStream output = new DataOutputStream(baos);
	protected DataInputStream input;
	
	public final int getPacketID(){
		return packetMap.indexOf(this.getClass());
	}
	
	public final void setData(byte[] bytes) throws Exception{
		input = new DataInputStream(new ByteArrayInputStream(bytes));
		input.readInt(); // skip packet id bytes in array.
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
	
	public final void sendToPlayer(EntityPlayer player){
		Packet250CustomPayload payload = getPayload();
		if(payload != null) PacketDispatcher.sendPacketToPlayer(payload, (Player)player);
	}
	
	public final void sendToAllInDim(int dim){
		Packet250CustomPayload payload = getPayload();
		if(payload != null) PacketDispatcher.sendPacketToAllInDimension(payload, dim);
	}
	
	protected abstract void onBeforeSend() throws Exception;
	public abstract void onReceive(EntityPlayer player, Side side) throws Exception;
}