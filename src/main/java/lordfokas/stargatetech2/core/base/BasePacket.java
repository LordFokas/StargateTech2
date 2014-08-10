package lordfokas.stargatetech2.core.base;

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
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import lordfokas.stargatetech2.core.packet.PacketOpenGUI;
import lordfokas.stargatetech2.core.packet.PacketToggleMachineFace;
import lordfokas.stargatetech2.core.packet.PacketUpdateBusAddress;
import lordfokas.stargatetech2.core.packet.PacketUpdateBusEnabled;
import lordfokas.stargatetech2.core.packet.PacketUpdateMachineColors;
import lordfokas.stargatetech2.core.util.ByteUtil;
import lordfokas.stargatetech2.core.util.PacketHandler;
import lordfokas.stargatetech2.enemy.packet.PacketExceptionsUpdate;
import lordfokas.stargatetech2.enemy.packet.PacketPermissionsUpdate;
import lordfokas.stargatetech2.transport.packet.PacketActivateRings;
import lordfokas.stargatetech2.transport.packet.PacketPrintAddress;
import lordfokas.stargatetech2.transport.packet.PacketWormhole;
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
		packetMap.add(PacketPrintAddress.class);
		packetMap.add(PacketUpdateMachineColors.class);
		packetMap.add(PacketToggleMachineFace.class);
		packetMap.add(PacketUpdateBusAddress.class);
		packetMap.add(PacketUpdateBusEnabled.class);
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
	
	public final S3FPacketCustomPayload getPayload(){
		try {
			output.writeInt(getPacketID());
			onBeforeSend();
			return new S3FPacketCustomPayload(PacketHandler.STARGATE_CHANNEL, baos.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public final void sendToServer(){
		S3FPacketCustomPayload payload = getPayload();
		if(payload != null) PacketDispatcher.sendPacketToServer(payload);
	}
	
	public final void sendToPlayer(EntityPlayerMP player){
		S3FPacketCustomPayload payload = getPayload();
		if(payload != null) player.playerNetServerHandler.sendPacket(payload);
	}
	
	public final void sendToAllInDim(int dim){
		S3FPacketCustomPayload payload = getPayload();
		if(payload != null) PacketDispatcher.sendPacketToAllInDimension(payload, dim);
	}
	
	protected abstract void onBeforeSend() throws Exception;
	public abstract void onReceive(EntityPlayer player, Side side) throws Exception;
}