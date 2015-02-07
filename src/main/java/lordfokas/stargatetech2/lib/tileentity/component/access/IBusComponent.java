package lordfokas.stargatetech2.lib.tileentity.component.access;

import lordfokas.stargatetech2.api.bus.IBusDevice;
import lordfokas.stargatetech2.api.bus.IBusInterface;
import lordfokas.stargatetech2.lib.tileentity.component.IAccessibleTileComponent;

public interface IBusComponent extends IAccessibleTileComponent{
	public void setBusDevice(IBusDevice device);
	public IBusInterface getInterface();
	public short getAddress();
	public void setAddress(short address);
	public boolean getEnabled();
	public void setEnabled(boolean enabled);
}
