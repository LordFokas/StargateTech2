package lordfokas.stargatetech2.integration.waila;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;

public abstract class ProviderBody extends ProviderBase{
	
	@Override
	public List<String> getWailaBody(ItemStack stack, List<String> list, IWailaDataAccessor accessor, IWailaConfigHandler configHandler) {
		addToBody(stack, list, accessor, configHandler);
		return list;
	}
	
	public abstract void addToBody(ItemStack stack, List<String> list, IWailaDataAccessor accessor, IWailaConfigHandler configHandler);
}
