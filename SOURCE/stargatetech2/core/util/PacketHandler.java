package stargatetech2.core.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import stargatetech2.core.base.BasePacket;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class PacketHandler implements IPacketHandler {
	public final static PacketHandler server = new PacketHandler(Side.SERVER);
	public final static PacketHandler client = new PacketHandler(Side.CLIENT);
	public final static String STARGATE_CHANNEL = "StargateTech2";
	
	private final Side side;
	private PacketHandler(Side s){ side = s; }
	
	public void register(){
		NetworkRegistry.instance().registerChannel(this, STARGATE_CHANNEL, side);
	}
	
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player){
		BasePacket rcvPacket = BasePacket.fromData(packet.data);
		if(rcvPacket != null){
			try{
				rcvPacket.setData(packet.data);
				rcvPacket.onReceive((EntityPlayer) player, side);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}