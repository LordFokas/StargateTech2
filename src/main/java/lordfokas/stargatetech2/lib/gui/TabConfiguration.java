package lordfokas.stargatetech2.lib.gui;

import lordfokas.stargatetech2.lib.tileentity.TileEntityMachine;
import lordfokas.stargatetech2.util.Stacks;

public class TabConfiguration extends BaseTab {
	private TileEntityMachine machine;
	
	public TabConfiguration(BaseGUI gui, TileEntityMachine machine) {
		super(gui, "Configuration", Stacks.coilNaq, 0xFF40C040);
		this.setSize(100, 110);
		this.machine = machine;
	}

	@Override
	protected void drawTab() {
		
	}

	@Override
	public void addHandlers() {
		
	}
}