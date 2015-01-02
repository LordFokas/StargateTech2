package lordfokas.stargatetech2.integration.waila;

import java.util.List;

import lordfokas.stargatetech2.api.bus.IBusDevice;
import lordfokas.stargatetech2.api.bus.IBusInterface;
import lordfokas.stargatetech2.automation.AddressHelper;
import lordfokas.stargatetech2.automation.BusInterface;
import lordfokas.stargatetech2.core.Helper;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import cofh.api.energy.IEnergyHandler;

public class CommonDataProvider extends Provider.Body{
	@Override
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
	}
}