package stargatetech2.core.gui;

import buildcraft.api.power.PowerHandler.PowerReceiver;
import stargatetech2.common.base.BaseContainer;
import stargatetech2.common.base.BaseGUI;
import stargatetech2.common.base.BaseGauge.PowerGauge;
import stargatetech2.common.reference.BlockReference;
import stargatetech2.common.reference.TextureReference;
import stargatetech2.core.tileentity.TileNaquadahCapacitor;

public class GUINaquadahCapacitor extends BaseGUI {
	private TileNaquadahCapacitor naquadahCapacitor;
	private HugePowerGauge power;
	
	private static class HugePowerGauge implements IGauge{
		private final int x, y, w, h;
		private BaseGUI master;
		private PowerReceiver capacitor;
		
		public HugePowerGauge(PowerReceiver powerReceiver){
			this.capacitor = powerReceiver;
			x = 7;
			y = 22;
			w = 160;
			h = 24;
		}
		
		@Override
		public void register(BaseGUI gui) {
			master = gui;
		}

		@Override
		public void renderGauge() {
			float f = capacitor.getEnergyStored() / capacitor.getMaxEnergyStored();
			master.bindBGImage();
			master.drawLocalQuad(x, y, 192F, 192F + (64F * f), 0F, 10F, ((float)w) * f, h);
		}
		
		// Doesn't have a tooltip
		@Override public void renderTooltip(){}
		
	}
	
	public GUINaquadahCapacitor(ContainerNaquadahCapacitor container) {
		super(container, 174, 74, false);
		naquadahCapacitor = container.nc;
		bgImage = TextureReference.GUI_NAQUADAH_CAPACITOR;
		power = new HugePowerGauge(naquadahCapacitor.getPowerReceiver(null));
		addGauge(power);
	}
	
	@Override
	protected void drawForeground(){
		PowerReceiver r = naquadahCapacitor.getPowerReceiver(null);
		bindImage(TextureReference.getTexture("blocks/" + BlockReference.NAQUADAH_CAPACITOR + ".png"));
		drawQuad(4, 3.5F, 0, 1, 0, 1, 8, 8);
		drawLeft("Naquadah Capacitor", 16, 4, 0x444444);
		drawLeft("Tier " + naquadahCapacitor.getTierName(), 135, 7, 0x333333);
		drawLeft(String.format("Power: %d / %d MJ", (int)r.getEnergyStored(), (int)r.getMaxEnergyStored()), 7, 52, 0x444444);
		drawLeft("Power Rate: " + (int)r.getMaxEnergyReceived() + " MJ/t", 7, 62, 0x444444);
	}
}