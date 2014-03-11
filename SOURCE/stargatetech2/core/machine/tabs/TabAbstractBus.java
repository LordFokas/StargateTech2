package stargatetech2.core.machine.tabs;

import stargatetech2.core.base.BaseTab;
import stargatetech2.core.util.Stacks;

public class TabAbstractBus extends BaseTab{
	public TabAbstractBus() {
		super("Abstract Bus", Stacks.busCable, TabColor.BLUE);
	}

	@Override
	public int getSizeY() {
		return 60;
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
	}
}
