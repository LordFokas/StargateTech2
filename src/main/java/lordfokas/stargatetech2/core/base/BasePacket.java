package lordfokas.stargatetech2.core.base;

import io.netty.buffer.ByteBuf;

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
import lordfokas.stargatetech2.core.reference.ModReference;
import lordfokas.stargatetech2.core.util.ByteUtil;
import lordfokas.stargatetech2.enemy.packet.PacketExceptionsUpdate;
import lordfokas.stargatetech2.enemy.packet.PacketPermissionsUpdate;
import lordfokas.stargatetech2.transport.packet.PacketActivateRings;
import lordfokas.stargatetech2.transport.packet.PacketPrintAddress;
import lordfokas.stargatetech2.transport.packet.PacketWormhole;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public abstract class BasePacket implements IMessage{
	/** Marks packets the server sends to the clients. */
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.SOURCE)
	public @interface ServerToClient{}
	
	/** Marks packets the client sends to the server. */
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.SOURCE)
	public @interface ClientToServer{}
	
	private static ArrayList<Class<? extends BasePacket>> packetMap = new ArrayList<Class<? extends BasePacket>>();
	private static SimpleNetworkWrapper network;
	private ByteArrayOutputStream baos;
	private ByteArrayInputStream bais;
	protected DataOutputStream output;
	protected DataInputStream input;
	private BasePacket packet;
	
	private static BasePacket fromData(byte[] data){
		int pktID = ByteUtil.readInt(data, 0);
		Class<? extends BasePacket> pktClass = packetMap.get(pktID);
		try {
			return pktClass.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	protected final int getPacketID(){
		return packetMap.indexOf(this.getClass());
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
	
	public static void registerAll(){
		network = NetworkRegistry.INSTANCE.newSimpleChannel(ModReference.MOD_ID);
		network.registerMessage(PacketHandler.Server.class, BasePacket.class, 0, Side.SERVER);
		network.registerMessage(PacketHandler.Client.class, BasePacket.class, 0, Side.CLIENT);
	}
	
	private static class PacketHandler implements IMessageHandler<BasePacket, BasePacket>{
		private final Side side;
		
		protected PacketHandler(Side side){
			this.side = side;
		}
		
		@Override public BasePacket onMessage(BasePacket message, MessageContext ctx) {
			try{ return message.unserialize(ctx.getServerHandler().playerEntity, side); }
			catch(Exception e){ e.printStackTrace(); }
			return null;
		}
		
		private static final class Client extends PacketHandler{
			public Client(){
				super(Side.CLIENT);
			}
		}
		
		private static final class Server extends PacketHandler{
			public Server(){
				super(Side.SERVER);
			}
		}
	}
	
	protected abstract BasePacket unserialize(EntityPlayerMP p, Side s) throws Exception;
	protected abstract void serialize() throws Exception;
	
	@Override
	public final void toBytes(ByteBuf buf){
		try{
			baos = new ByteArrayOutputStream();
			output = new DataOutputStream(baos);
			serialize();
			buf.writeBytes(baos.toByteArray());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public final void fromBytes(ByteBuf buf){
		byte[] data = buf.array();
		packet = fromData(data);
		packet.input = new DataInputStream(new ByteArrayInputStream(data));
	}
	
	public final void sendToAllClients(){ network.sendToAll(this); }
	public final void sendToClient(EntityPlayerMP player){ network.sendTo(this, player); }
	public final void sendToClientsNear(TargetPoint point){ network.sendToAllAround(this, point); }
	public final void sendToClientsInDim(int dim){ network.sendToDimension(this, dim); }
	public final void sendToServer(){ network.sendToServer(this); }
}