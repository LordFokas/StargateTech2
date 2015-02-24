package lordfokas.stargatetech2.lib.packet;

import io.netty.buffer.ByteBuf;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;

import lordfokas.stargatetech2.ZZ_THRASH.PacketToggleMachineFace__THRASH;
import lordfokas.stargatetech2.ZZ_THRASH.PacketUpdateMachineColors__THRASH;
import lordfokas.stargatetech2.modules.automation.PacketUpdateBusAddress;
import lordfokas.stargatetech2.modules.automation.PacketUpdateBusEnabled;
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
	@Deprecated
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ServerToClient{}
	
	/** Marks packets the client sends to the server. */
	@Deprecated
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ClientToServer{}
	
	/** Marks the sides that receive this packet. Mandatory annotation. */
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface SidesReceivedOn{
		Side[] value() default {};
	}
	
	private static ArrayList<Class<? extends BasePacket<?,? extends IMessage>>> packetMap = new ArrayList();
	private static SimpleNetworkWrapper network;
	protected ByteBuf output;
	protected ByteBuf input;
	
	protected final int getPacketID(){
		return packetMap.indexOf(this.getClass());
	}
	
	public BasePacket(){
		if(getPacketID() == -1){
			throw new RuntimeException("Packet isn't registered! (" + getClass().getName() + ")");
		}
	}
	
	static{
		// pre v0.8 packets
		packetMap.add(PacketPermissionsUpdate.class);
		packetMap.add(PacketExceptionsUpdate.class);
		packetMap.add(PacketActivateRings.class);
		packetMap.add(PacketWormhole.class);
		packetMap.add(PacketPrintAddress.class);
		packetMap.add(PacketUpdateBusAddress.class);
		packetMap.add(PacketUpdateBusEnabled.class);
		
		// v0.8+ packets
		packetMap.add(PacketMachineConfiguration.class);
		
		// old shit that needs to be deleted.
		packetMap.add(PacketUpdateMachineColors__THRASH.class);
		packetMap.add(PacketToggleMachineFace__THRASH.class);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void registerAll(){
		network = NetworkRegistry.INSTANCE.newSimpleChannel(ModReference.MOD_ID);
		
		for( Class clazz: packetMap ){
			boolean isAnnotated = false;
			boolean registeredSide = false;
			Side[] sides = new Side[]{};
			if(clazz.isAnnotationPresent(SidesReceivedOn.class)){
				isAnnotated = true;
				sides = ((SidesReceivedOn) clazz.getAnnotation(SidesReceivedOn.class)).value();
			}else{
				sides = new Side[2];
				if(clazz.isAnnotationPresent(ServerToClient.class)){
					isAnnotated = true;
					sides[0] = Side.CLIENT;
				}
				if(clazz.isAnnotationPresent(ClientToServer.class)){
					isAnnotated = true;
					sides[1] = Side.SERVER;
				}
			}
			
			if(sides != null && sides.length > 0){
				for(Side side : sides){
					if(side != null){
						registeredSide = true;
						registerPacket(clazz, side);
					}
				}
			}
			if(!isAnnotated) throw new RuntimeException("Attempted to register a packet class that isn't annotated! (" + clazz.getName() + ")");
			if(!registeredSide) throw new RuntimeException("A Packet MUST have at least 1 receiving side! (" + clazz.getName() + ")");
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