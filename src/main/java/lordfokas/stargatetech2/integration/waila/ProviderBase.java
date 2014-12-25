package lordfokas.stargatetech2.integration.waila;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;

public class ProviderBase implements IWailaDataProvider{

	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler configHandler) {
		return null;
	}

	@Override
	public List<String> getWailaHead(ItemStack stack, List<String> list, IWailaDataAccessor accessor, IWailaConfigHandler configHandler) {
		return list;
	}

	@Override
	public List<String> getWailaBody(ItemStack stack, List<String> list, IWailaDataAccessor accessor, IWailaConfigHandler configHandler) {
		return list;
	}

	@Override
	public List<String> getWailaTail(ItemStack stack, List<String> list, IWailaDataAccessor accessor, IWailaConfigHandler configHandler) {
		return list;
	}

	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity tileEntity, NBTTagCompound nbt, World world, int x, int y, int z) {
		return nbt;
	}
	
}
