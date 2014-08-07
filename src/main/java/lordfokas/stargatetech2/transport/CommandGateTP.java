package lordfokas.stargatetech2.transport;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import lordfokas.stargatetech2.api.stargate.Address;
import lordfokas.stargatetech2.core.util.Vec3Int;
import lordfokas.stargatetech2.transport.stargates.AddressMapping;
import lordfokas.stargatetech2.transport.stargates.StargateNetwork;
import lordfokas.stargatetech2.transport.util.Teleporter;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandGateTP extends CommandBase {

	@Override
	public String getCommandName() {
		return "tpgate";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		List<String> prms = Arrays.asList(astring);
		
		if (astring.length < 3 || astring.length > 4) {
			throw new WrongUsageException("commands.tpgate.usage", new Object[0]);
		}
		
		EntityPlayerMP epl;
		if (astring.length == 4) {
			epl = getPlayer(icommandsender, prms.remove(0));
			if (epl == null) throw new PlayerNotFoundException();
		} else {
			epl = getCommandSenderAsPlayer(icommandsender);
		}
		
		Address address = StargateNetwork.instance().parseAddress(StringUtils.join(prms, " "));
		if (address == null) throw new SyntaxErrorException("commands.tpgate.badaddress");
		
		AddressMapping amap = StargateNetwork.instance().getAddressMapping(address);
		if (amap == null) throw new CommandException("commands.tpgate.nogate");
		
		Teleporter.teleport(epl.worldObj, epl, MinecraftServer.getServer().worldServerForDimension(amap.getDimension()), new Vec3Int(amap.getXCoord(), amap.getYCoord()+1, amap.getZCoord()), epl.cameraYaw);
	}

}
