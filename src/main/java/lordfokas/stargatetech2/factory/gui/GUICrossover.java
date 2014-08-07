package lordfokas.stargatetech2.factory.gui;

import lordfokas.stargatetech2.core.base.BaseGUI;
import lordfokas.stargatetech2.core.reference.TextureReference;

public class GUICrossover extends BaseGUI {

	public GUICrossover(ContainerCrossover container) {
		super(container, 156, 136, false);
		super.bgImage = TextureReference.GUI_CROSSOVER;
	}

}
