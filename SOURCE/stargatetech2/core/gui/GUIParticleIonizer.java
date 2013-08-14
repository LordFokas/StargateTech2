package stargatetech2.core.gui;

import stargatetech2.common.base.BaseContainer;
import stargatetech2.common.base.BaseGUI;
import stargatetech2.common.reference.BlockReference;
import stargatetech2.common.reference.TextureReference;

public class GUIParticleIonizer extends BaseGUI {
	public GUIParticleIonizer(BaseContainer container) {
		super(container, 200, 100);
		bgImage = TextureReference.GUI_PARTICLE_IONIZER;
	}
	
	@Override
	protected void drawForeground(){
		bindImage(TextureReference.getTexture("blocks/" + BlockReference.PARTICLE_IONIZER + ".png"));
		drawQuad(4, 3.5F, 0, 1, 0, 1, 8, 8);
		drawLeft("Particle Ionizer", 16, 4, 0x444444);
	}
}