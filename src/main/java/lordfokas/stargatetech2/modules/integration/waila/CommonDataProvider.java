package lordfokas.stargatetech2.modules.integration.waila;

public class CommonDataProvider /* extends Provider.Body */{
	/*@Override
	public void addToBody(ItemStack stack, List<String> list, IWailaDataAccessor accessor, IWailaConfigHandler configHandler){
		TileEntity te = accessor.getTileEntity();
		
		if(te instanceof IBusDevice){
			IBusDevice busDevice = (IBusDevice) te;
			IBusInterface iface = null;
			for(int i = 0; i < 6; i++){
				IBusInterface[] ifaces = busDevice.getInterfaces(i);
				if(ifaces != null && ifaces.length > 0){
					iface = ifaces[0];
					break;
				}
			}
			if(iface != null){
				String addr = AddressHelper.convert(((BusInterface)iface).getAddress());
				list.add("Abstract Bus: 0x" + addr);
			}
		}
		
		if(te instanceof IEnergyHandler){
			IEnergyHandler energyHandler = ((IEnergyHandler)te);
			int curr = energyHandler.getEnergyStored(null);
			int max = energyHandler.getMaxEnergyStored(null);
			list.add(Helper.prettyNumber(curr) + " / " + Helper.prettyNumber(max) + " RF");
		}
	}*/
}