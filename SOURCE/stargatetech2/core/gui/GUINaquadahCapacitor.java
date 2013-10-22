package stargatetech2.core.gui;

import stargatetech2.common.base.BaseContainer;
import stargatetech2.common.base.BaseGUI;
import stargatetech2.common.base.BaseGauge.PowerGauge;
import stargatetech2.common.reference.BlockReference;
import stargatetech2.common.reference.TextureReference;
import stargatetech2.core.tileentity.TileNaquadahCapacitor;

public class GUINaquadahCapacitor extends BaseGUI {
	private TileNaquadahCapacitor naquadahCapacitor;
	private PowerGauge power;

	public GUINaquadahCapacitor(ContainerNaquadahCapacitor container) {
		super(container, 174, 96, false);
		naquadahCapacitor = container.nc;
		bgImage = TextureReference.GUI_NAQUADAH_CAPACITOR;
		power = new PowerGauge(9, 24, naquadahCapacitor.getMaxPower());
		addGauge(power);
	}
	
	@Override
	protected void drawForeground(){
		bindImage(TextureReference.getTexture("blocks/" + BlockReference.NAQUADAH_CAPACITOR + ".png"));
		drawQuad(4, 3.5F, 0, 1, 0, 1, 8, 8);
		drawLeft("Naquadah Capacitor", 16, 4, 0x444444);
		drawLeft("Tier " + naquadahCapacitor.getTierName(), 135, 7, 0x333333);
	}
	
	@Override
	protected void updateGauges(){
		power.setCurrentValue(naquadahCapacitor.getPower());
	}
}