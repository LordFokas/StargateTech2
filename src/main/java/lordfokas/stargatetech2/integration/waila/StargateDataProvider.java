package lordfokas.stargatetech2.integration.waila;

import java.util.List;

import lordfokas.stargatetech2.api.stargate.ITileStargate;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.SpecialChars;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class StargateDataProvider extends Provider.Body{
	@Override
	public void addToBody(ItemStack stack, List<String> list, IWailaDataAccessor accessor, IWailaConfigHandler configHandler) {
		TileEntity te = accessor.getTileEntity();
		if(te instanceof ITileStargate){
			list.add(SpecialChars.GOLD + ((ITileStargate)te).getClientAddress());
		}
	}
}