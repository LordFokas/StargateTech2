package stargatetech2.core.machine.tabs;

import stargatetech2.api.bus.IBusDevice;
import stargatetech2.automation.bus.AddressHelper;
import stargatetech2.core.base.BaseGUI.Arrow;
import stargatetech2.core.base.BaseGUI.Toggle;
import stargatetech2.core.base.BaseTab;
import stargatetech2.core.packet.PacketUpdateBusAddress;
import stargatetech2.core.packet.PacketUpdateBusEnabled;
import stargatetech2.core.util.Stacks;

public class TabAbstractBus extends BaseTab{
	private static final int TOGGLE_X = 20;
	private static final int TOGGLE_Y = 27;
	private static final int ADDR_X = 51;
	private static final int ADDR_Y = 42;
	
	private short address;
	private Toggle toggle;
	private ISyncBusDevice device;
	
	public static interface ISyncBusDevice extends IBusDevice{
		public void setEnabled(boolean enabled);
		public boolean getEnabled();
		public void setAddress(short addr);
		public short getAddress();
	}
	
	public TabAbstractBus(ISyncBusDevice device) {
		super("Abstract Bus", Stacks.busCable, TabColor.BLUE);
		this.device = device;
		update();
	}
	
	@Override
	public int getSizeY() {
		return 70;
	}

	@Override
	public void render() {
		gui.drawDarkerArea(getColor(), TOGGLE_X - 4, TOGGLE_Y - 4, 74, 49);
		gui.drawSlot(getColor(), TOGGLE_X, TOGGLE_Y, 16, 8);
		gui.drawToggle(toggle, TOGGLE_X, TOGGLE_Y);
		gui.drawLeft(toggle == Toggle.ON ? "Enabled" : "Disabled", TOGGLE_X + 20, TOGGLE_Y + 1, 0xEEEEEE);
		
		gui.drawRight("Addr:", ADDR_X - 4, ADDR_Y + 9, 0xEEEEEE);
		for(int i = 0; i < 4; i++){
			gui.drawSlot(getColor(), ADDR_X + (9 * i), ADDR_Y + 8, 7, 9);
			gui.drawLeft(getDigit(i), ADDR_X + 1 + (9 * i), ADDR_Y + 9, 0xFFFFFF);
			gui.drawArrow(Arrow.UP, ADDR_X + (9 * i), ADDR_Y - 1);
			gui.drawArrow(Arrow.DOWN, ADDR_X + (9 * i), ADDR_Y + 18);
		}
	}
	
	@Override
	public boolean handleClick(int x, int y){
		if(elementHit(TOGGLE_X, TOGGLE_Y, x, y, 16, 8)){
			toggle = toggle.invert();
			gui.playClick(toggle == Toggle.ON ? 0.85F : 0.65F);
			PacketUpdateBusEnabled update = new PacketUpdateBusEnabled();
			update.x = device.getXCoord();
			update.y = device.getYCoord();
			update.z = device.getZCoord();
			update.enabled = (toggle == Toggle.ON);
			update.sendToServer();
		}
		for(int i = 0; i < 4; i++){
			if(elementHit(ADDR_X + (9 * i), ADDR_Y - 1, x, y, 8, 8)){
				up(3 - i);
			}else if(elementHit(ADDR_X + (9 * i), ADDR_Y + 18, x, y, 8, 8)){
				down(3 - i);
			}
		}
		return !elementHit(TOGGLE_X - 4, TOGGLE_Y - 4, x, y, 74, 49);
	}
	
	private void up(int digit){
		int val = (int) Math.pow(16, digit);
		String d = getDigit(3 - digit);
		if(d.equalsIgnoreCase("F")){
			address -= (val * 15);
		}else{
			address += val;
		}
		update(address);
		gui.playClick(0.8F);
	}
	
	private void down(int digit){
		int val = (int) Math.pow(16, digit);
		String d = getDigit(3 - digit);
		if(d.equalsIgnoreCase("0")){
			address += (val * 15);
		}else{
			address -= val;
		}
		update(address);
		gui.playClick(0.7F);
	}
	
	private String getDigit(int digit){
		String addr = AddressHelper.convert(address);
		return addr.substring(digit, digit + 1);
	}
	
	private void update(){
		address = device.getAddress();
		toggle = device.getEnabled() ? Toggle.ON : Toggle.OFF;
	}
	
	private void update(short addr){
		PacketUpdateBusAddress update = new PacketUpdateBusAddress();
		update.x = device.getXCoord();
		update.y = device.getYCoord();
		update.z = device.getZCoord();
		update.address = addr;
		update.sendToServer();
	}
}