package lordfokas.stargatetech2.lib.packet;

import io.netty.buffer.ByteBuf;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;

import lordfokas.stargatetech2.modules.automation.PacketUpdateBusAddress;
import lordfokas.stargatetech2.modules.automation.PacketUpdateBusEnabled;
import lordfokas.stargatetech2.modules.core.packets.PacketToggleMachineFace;
import lordfokas.stargatetech2.modules.core.packets.PacketUpdateMachineColors;
import lordfokas.stargatetech2.modules.enemy.PacketExceptionsUpdate;
import lordfokas.stargatetech2.modules.enemy.PacketPermissionsUpdate;
import lordfokas.stargatetech2.modules.transport.PacketActivateRings;
import lordfokas.stargatetech2.modules.transport.PacketPrintAddress;
import lordfokas.stargatetech2.modules.transport.PacketWormhole;
import lordfokas.stargatetech2.reference.ModReference;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class BasePacket<T extends BasePacket<T,RES>,RES extends IMessage> implements IMessage,IMessageHandler<T, RES>{
	/** Marks packets the server sends to the clients. */
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ServerToClient{}
	
	/** Marks packets the client sends to the server. */
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ClientToServer{}
	//Class<T extends BasePacket<T,IMessage>>
	private static ArrayList<Class<? extends BasePacket<?,? extends IMessage>>> packetMap = new ArrayList<Class<? extends BasePacket<?,? extends IMessage>>>();
	private static SimpleNetworkWrapper network;
	protected ByteBuf output;
	protected ByteBuf input;
	
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void registerAll(){
		network = NetworkRegistry.INSTANCE.newSimpleChannel(ModReference.MOD_ID);
		
		for( Class clazz: packetMap ){
			if(clazz.isAnnotationPresent(ServerToClient.class)){
				registerPacket(clazz, Side.CLIENT);
			}
			
			if(clazz.isAnnotationPresent(ClientToServer.class)){
				registerPacket(clazz, Side.SERVER);
			}
		}
	}
	
	
	@Override 
	public RES onMessage(T message, MessageContext ctx) {
		try{ 
			if(ctx.side == Side.SERVER){
				return onMessageServer(message,ctx);
			}else{
				return onMessageClient(message,ctx);
			}
		}catch(Exception e){ e.printStackTrace(); }
		return null;
	}
	
	public RES onMessageServer(T message, MessageContext ctx) throws Exception{
		return message.unserialize(ctx.getServerHandler().playerEntity, ctx.side);
	}
	
	@SideOnly(Side.CLIENT)
	public RES onMessageClient(T message, MessageContext ctx) throws Exception{
		return message.unserialize(Minecraft.getMinecraft().thePlayer, ctx.side);
	}
	
	@SuppressWarnings("unchecked")
	private static <Z extends BasePacket<Z,Q>, Q extends IMessage> void registerPacket(Class<Z> clazz,Side side){
		network.registerMessage(clazz, clazz, packetMap.indexOf(clazz), side);
	}
	
	protected abstract RES unserialize(EntityPlayer p, Side s) throws Exception;
	protected abstract void serialize() throws Exception;
	
	@Override
	public final void toBytes(ByteBuf buf){
		try{
			output = buf;
			serialize();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public final void fromBytes(ByteBuf buf){
		input = buf;
	}
	
	public final void sendToAllClients(){ network.sendToAll(this); }
	public final void sendToClient(EntityPlayerMP player){ network.sendTo(this, player); }
	public final void sendToClientsNear(TargetPoint point){ network.sendToAllAround(this, point); }
	public final void sendToClientsInDim(int dim){ network.sendToDimension(this, dim); }
	public final void sendToServer(){ network.sendToServer(this); }
}