package lordfokas.stargatetech2.core.api;

import lordfokas.stargatetech2.api.IFactory;
import lordfokas.stargatetech2.api.bus.IBusDevice;
import lordfokas.stargatetech2.api.bus.IBusDriver;
import lordfokas.stargatetech2.api.bus.IBusInterface;
import lordfokas.stargatetech2.automation.BusInterface;

public class Factory implements IFactory{
	@Override
	public IBusInterface getIBusInterface(IBusDevice device, IBusDriver driver) {
		return new BusInterface(device, driver);
	}
}
