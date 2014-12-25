package lordfokas.stargatetech2.integration.waila;

import java.util.List;

import lordfokas.stargatetech2.api.stargate.ITileStargate;
import lordfokas.stargatetech2.core.Helper;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.SpecialChars;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import cofh.api.energy.IEnergyHandler;

public class StargateDataProvider extends ProviderBody{
	@Override
	public void addToBody(ItemStack stack, List<String> list, IWailaDataAccessor accessor, IWailaConfigHandler configHandler) {
		TileEntity te = accessor.getTileEntity();
		if(te instanceof ITileStargate){
			list.add(SpecialChars.GOLD + ((ITileStargate)te).getClientAddress());
		}
	}
}