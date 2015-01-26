package lordfokas.stargatetech2.modules.automation;

import lordfokas.stargatetech2.api.bus.IBusDevice;

public interface ISyncBusDevice extends IBusDevice{
	public void setEnabled(boolean enabled);
	public boolean getEnabled();
	public void setAddress(short addr);
	public short getAddress();
}
