package stargatetech2.core.api;

import stargatetech2.api.IFactory;
import stargatetech2.api.bus.IBusDevice;
import stargatetech2.api.bus.IBusDriver;
import stargatetech2.api.bus.IBusInterface;
import stargatetech2.automation.bus.BusInterface;

public class Factory implements IFactory{
	@Override
	public IBusInterface getIBusInterface(IBusDevice device, IBusDriver driver) {
		return new BusInterface(device, driver);
	}
}
