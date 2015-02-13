package lordfokas.stargatetech2.lib.tileentity.component.base;

import lordfokas.stargatetech2.api.StargateTechAPI;
import lordfokas.stargatetech2.api.bus.IBusDevice;
import lordfokas.stargatetech2.api.bus.IBusInterface;
import lordfokas.stargatetech2.lib.tileentity.ISyncedGUI;
import lordfokas.stargatetech2.lib.tileentity.component.SidedComponent;
import lordfokas.stargatetech2.lib.tileentity.component.access.IBusComponent;
import lordfokas.stargatetech2.modules.automation.ISyncBusDriver;
import net.minecraft.nbt.NBTTagCompound;

public class BusComponent extends SidedComponent implements IBusComponent, ISyncedGUI.Flow{
	private static final int[] KEYS = new int[]{0, 1};
	private IBusInterface iface;
	private ISyncBusDriver driver;
	
	public BusComponent(ISyncBusDriver driver){
		this.driver = driver;
	}
	
	public void setBusDevice(IBusDevice device){
		this.iface = StargateTechAPI.api().getFactory().getIBusInterface(device, driver);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		driver.setInterfaceAddress(nbt.getShort("address"));
		driver.setInterfaceEnabled(nbt.getBoolean("enabled"));
		iface.readFromNBT(nbt, "interface");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setShort("address", driver.getInterfaceAddress());
		nbt.setBoolean("enabled", driver.isInterfaceEnabled());
		iface.writeToNBT(nbt, "interface");
		return nbt;
	}

	@Override
	public IBusInterface getInterface() {
		return iface;
	}

	@Override
	public short getAddress() {
		return driver.getInterfaceAddress();
	}

	@Override
	public void setAddress(short address) {
		driver.setInterfaceAddress(address);
	}

	@Override
	public boolean getEnabled() {
		return driver.isInterfaceEnabled();
	}

	@Override
	public void setEnabled(boolean enabled) {
		driver.setInterfaceEnabled(enabled);
	}

	@Override
	public int[] getKeyArray() {
		return KEYS;
	}

	@Override
	public int getValue(int key) {
		return key == 0 ? driver.getInterfaceAddress() : driver.isInterfaceEnabled() ? 1 : 0;
	}

	@Override
	public void setValue(int key, int val) {
		if(key == 0) driver.setInterfaceAddress((short)val);
		else driver.setInterfaceEnabled(val != 0);
	}
}
