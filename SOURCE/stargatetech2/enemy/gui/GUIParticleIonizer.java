package stargatetech2.enemy.gui;

import java.util.List;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;
import stargatetech2.core.api.ParticleIonizerRecipes;
import stargatetech2.core.api.ParticleIonizerRecipes.IonizerRecipe;
import stargatetech2.core.base.BaseGUI;
import stargatetech2.core.base.BaseGauge.PowerGauge;
import stargatetech2.core.base.BaseGauge.TankGauge;
import stargatetech2.core.machine.FaceColor;
import stargatetech2.core.machine.tabs.TabConfiguration;
import stargatetech2.core.machine.tabs.TabMachineRecipes;
import stargatetech2.core.machine.tabs.TabMachineRecipes.IMachineRecipe;
import stargatetech2.core.reference.BlockReference;
import stargatetech2.core.reference.TextureReference;
import stargatetech2.core.util.Helper;
import stargatetech2.enemy.tileentity.TileParticleIonizer;

public class GUIParticleIonizer extends BaseGUI {
	private TileParticleIonizer ionizer;
	private ProgressHover progressHover;
	private TankGauge fluidIonizable, ionizedParticles;
	private PowerGauge power;
	
	private static class MachineRecipe implements IMachineRecipe{
		private GUIParticleIonizer gui;
		private IonizerRecipe recipe;
		
		public MachineRecipe(GUIParticleIonizer gui, IonizerRecipe recipe){
			this.recipe = recipe;
			this.gui = gui;
		}

		@Override
		public void renderAt(int x, int y) {
			ItemStack solid = recipe.getSolid();
			if(solid != null){
				gui.drawStack(solid, x+1, y+1);
				gui.drawLeft(solid.getDisplayName(), x + 20, y + 1, 0xAAAAAA);
			}
			FluidStack fluid = recipe.getFluid();
			if(fluid != null){
				gui.drawIcon(x+1, y + 18, fluid.getFluid().getStillIcon(), TextureMap.locationBlocksTexture, 16);
				gui.drawLeft(fluid.getFluid().getLocalizedName(), x + 20, y + 26, 0xAAAAAA);
			}
			gui.drawLeft(Helper.prettyNumber(recipe.ions * recipe.time) + " mB", x + 25, y + 14, 0xEEEEEE);
		}
	}
	
	private static class ProgressHover implements IHoverHandler{
		public boolean isHover = false;
		public int x, y;
		
		@Override
		public void onHover(int x, int y) {
			isHover = true;
			this.x = x;
			this.y = y;
		}

		@Override
		public void onLeave() {
			isHover = false;
		}
	}
	
	public GUIParticleIonizer(ContainerParticleIonizer container) {
		super(container, 198, 184, false);
		super.bgImage = TextureReference.GUI_PARTICLE_IONIZER;
		ionizer = container.ionizer;
		progressHover = new ProgressHover();
		fluidIonizable = new TankGauge(29, 21, ionizer.fluidIonizable);
		ionizedParticles = new TankGauge(176, 11, ionizer.ionizedParticles);
		power = new PowerGauge(6, 21, ionizer.energy);
		super.addHoverHandler(progressHover, 115, 42, 52, 8);
		super.addGauge(fluidIonizable);
		super.addGauge(ionizedParticles);
		super.addGauge(power);
		List<IonizerRecipe> recipes = ParticleIonizerRecipes.recipes().getRecipes();
		IMachineRecipe[] tabRecipes = new IMachineRecipe[recipes.size()];
		for(int r = 0; r < recipes.size(); r ++){
			tabRecipes[r] = new MachineRecipe(this, recipes.get(r));
		}
		super.addTab(new TabMachineRecipes(tabRecipes));
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
		if(ionizer.hasColor(FaceColor.BLUE)){
			this.drawFrame(FaceColor.BLUE, 27, 19, 20, 68);
			this.drawFrame(FaceColor.BLUE, 51, 19, 56, 56);
		}
		if(ionizer.hasColor(FaceColor.ORANGE)){
			this.drawFrame(FaceColor.ORANGE, 174, 9, 20, 68);
		}
		IonizerRecipe recipe = ionizer.getRecipeInstance();
		int ticksLeft = ((ContainerParticleIonizer)this.inventorySlots).getWorkLeft();
		if(recipe != null){
			FluidStack fluid = recipe.getFluid();
			ItemStack solid = recipe.getSolid();
			if(fluid != null){
				drawIcon(124, 21, fluid.getFluid().getStillIcon(), TextureMap.locationBlocksTexture, 16);
			}
			if(solid != null){
				itemRenderer.renderItemAndEffectIntoGUI(fontRenderer, mc.renderEngine, solid, 151, 30);
			}
			float fill = 1F - ((float)ticksLeft) / ((float)recipe.time);
			this.bindBGImage();
			drawLocalQuad(115, 42, 204, 204 + (52F * fill), 0, 8, fill * 52F, 8);
			drawLeft(recipe.ions  + " mB/t", 123, 54, 0x444444);
			drawLeft(recipe.power + " RF/t", 123, 66, 0x444444);
		}else{
			drawLeft("---", 123, 54, 0x444444);
			drawLeft("---", 123, 66, 0x444444);
		}
		if(progressHover.isHover){
			if(recipe != null){
				drawHover(progressHover.x, progressHover.y, EnumChatFormatting.BLUE + "Time Left", Helper.ticks2time(ticksLeft));
			}else{
				drawHover(progressHover.x, progressHover.y, EnumChatFormatting.GRAY + "No work to do!");
			}
		}
	}
}