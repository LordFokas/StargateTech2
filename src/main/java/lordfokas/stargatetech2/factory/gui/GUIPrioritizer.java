package lordfokas.stargatetech2.factory.gui;

import lordfokas.stargatetech2.core.base.BaseGUI;
import lordfokas.stargatetech2.core.machine.tabs.TabConfiguration;
import lordfokas.stargatetech2.core.reference.TextureReference;

public class GUIPrioritizer extends BaseGUI {
	
	public GUIPrioritizer(ContainerPrioritizer container) {
		super(container, 156, 136, false);
		super.bgImage = TextureReference.GUI_PRIORITIZER;
		super.addTab(new TabConfiguration(container.prioritizer));
	}
}