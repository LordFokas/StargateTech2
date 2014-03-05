package stargatetech2.enemy.gui;

import stargatetech2.core.base.BaseGUI;
import stargatetech2.core.base.BaseGauge.PowerGauge;
import stargatetech2.core.base.BaseGauge.TankGauge;
import stargatetech2.core.machine.tabs.TabAbstractBus;
import stargatetech2.core.machine.tabs.TabConfiguration;
import stargatetech2.core.reference.BlockReference;
import stargatetech2.core.reference.TextureReference;
import stargatetech2.enemy.tileentity.TileParticleIonizer;

public class GUIParticleIonizer extends BaseGUI {
	private TileParticleIonizer ionizer;
	private TankGauge fluidIonizable, ionizedParticles;
	private PowerGauge power;
	
	public GUIParticleIonizer(ContainerParticleIonizer container) {
		super(container, 198, 184, false);
		super.bgImage = TextureReference.GUI_PARTICLE_IONIZER;
		ionizer = container.ionizer;
		fluidIonizable = new TankGauge(29, 21, ionizer.fluidIonizable);
		ionizedParticles = new TankGauge(176, 11, ionizer.ionizedParticles);
		power = new PowerGauge(6, 21, ionizer.energy);
		super.addGauge(fluidIonizable);
		super.addGauge(ionizedParticles);
		super.addGauge(power);
		super.addTab(new TabAbstractBus());
		super.addTab(new TabConfiguration(ionizer));
	}
	
	@Override
	protected void drawForeground(){
		bindImage(mc.thePlayer.getLocationSkin());
		drawQuad(183, 84.5F, 8F/64F, 16F/64F, 8F/32F, 16F/32F, 8, 8);
		bindImage(TextureReference.getTexture("blocks/" + BlockReference.PARTICLE_IONIZER + ".png"));
		drawQuad(4, 3.5F, 0, 1, 0, 1, 8, 8);
		drawLeft("Particle Ionizer", 16, 4, 0x444444);
		drawLeft("Inventory", 130, 85, 0x444444);
	}
}