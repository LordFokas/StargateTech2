package stargatetech2.enemy.gui;

import stargatetech2.core.api.ParticleIonizerRecipes;
import stargatetech2.core.api.ParticleIonizerRecipes.Recipe;
import stargatetech2.core.base.BaseGUI;
import stargatetech2.core.base.BaseGauge.PowerGauge;
import stargatetech2.core.base.BaseGauge.TankGauge;
import stargatetech2.core.reference.BlockReference;
import stargatetech2.core.reference.TextureReference;
import stargatetech2.enemy.tileentity.TileParticleIonizer;

public class GUIParticleIonizer extends BaseGUI {
	private TileParticleIonizer ionizer;
	private TankGauge ionTank;
	private PowerGauge power;
	
	public GUIParticleIonizer(ContainerParticleIonizer container) {
		super(container, 174, 184, false);
		ionizer = container.ionizer;
		bgImage = TextureReference.GUI_PARTICLE_IONIZER;
		ionTank = new TankGauge(127, 16, ionizer.getTankInfo(null)[0].capacity);
		power = new PowerGauge(151, 16, ionizer.getMaxEnergyStored(null));
		power.setCurrentValue(60000);
		addGauge(ionTank);
		addGauge(power);
	}
	
	@Override
	protected void drawForeground(){
		bindImage(mc.thePlayer.getLocationSkin());
		drawQuad(4, 84.5F, 8F/64F, 16F/64F, 8F/32F, 16F/32F, 8, 8);
		bindImage(TextureReference.getTexture("blocks/" + BlockReference.PARTICLE_IONIZER + ".png"));
		drawQuad(4, 3.5F, 0, 1, 0, 1, 8, 8);
		drawLeft("Particle Ionizer", 16, 4, 0x444444);
		drawLeft("Inventory", 16, 85, 0x444444);
		if(ionizer.consuming != null && ionizer.workTicks > 0){
			Recipe recipe = ParticleIonizerRecipes.getRecipe(ionizer.consuming);
			itemRenderer.renderItemAndEffectIntoGUI(fontRenderer, mc.renderEngine, ionizer.consuming, 94, 31);
			drawLeft("" + recipe.ions + " mB/t", 77, 52, 0x444444);
			drawLeft("" + recipe.power + " RF/t", 77, 64, 0x444444);
			
			float finish = 1F - ((float)ionizer.workTicks) / ((float)recipe.ticks);
			bindImage(bgImage);
			drawLocalQuad(67, 43, 204, 204F + (52F * finish), 0, 4, 52F * finish, 4);
		}else{
			drawLeft("--", 77, 52, 0x444444);
			drawLeft("--", 77, 64, 0x444444);
		}
	}
	
	@Override
	protected void updateGauges(){
		ionTank.setCurrentValue(ionizer.getIonAmount());
		power.setCurrentValue(ionizer.getEnergyStored(null));
	}
}