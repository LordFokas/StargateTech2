package lordfokas.stargatetech2.modules.integration.waila;

public abstract class Provider /*implements IWailaDataProvider*/ {

	/*@Override
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
	
	public static abstract class Body extends Provider{
		@Override
		public List<String> getWailaBody(ItemStack stack, List<String> list, IWailaDataAccessor accessor, IWailaConfigHandler configHandler) {
			addToBody(stack, list, accessor, configHandler);
			return list;
		}
		
		public abstract void addToBody(ItemStack stack, List<String> list, IWailaDataAccessor accessor, IWailaConfigHandler configHandler);
		
		@Override
		public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity tileEntity, NBTTagCompound nbt, World world, int x, int y, int z) {
			return null;
		}
	}
	
	public static abstract class NBT extends Provider{}*/
}
