package lordfokas.stargatetech2.modules.automation;

import lordfokas.stargatetech2.api.bus.IBusDriver;

public interface ISyncBusDriver extends IBusDriver{
	public void setInterfaceAddress(short address);
	public void setInterfaceEnabled(boolean enabled);
}
