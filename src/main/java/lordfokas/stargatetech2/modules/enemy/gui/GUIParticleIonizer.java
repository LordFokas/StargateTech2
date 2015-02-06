package lordfokas.stargatetech2.modules.enemy.gui;

import java.util.List;

import lordfokas.stargatetech2.lib.tileentity.faces.FaceColor;
import lordfokas.stargatetech2.modules.core.base__THRASH.BaseGUI__OLD_AND_FLAWED;
import lordfokas.stargatetech2.modules.core.base__THRASH.BaseGUI__OLD_AND_FLAWED.ITab.TabColor;
import lordfokas.stargatetech2.modules.core.base__THRASH.BaseGauge__OLD_AND_FLAWED.PowerGauge;
import lordfokas.stargatetech2.modules.core.base__THRASH.BaseGauge__OLD_AND_FLAWED.TankGauge;
import lordfokas.stargatetech2.modules.core.machine__TRASH.tabs__THRASH.TabConfiguration__THRASH;
import lordfokas.stargatetech2.modules.core.machine__TRASH.tabs__THRASH.TabMachineRecipes__THRASH;
import lordfokas.stargatetech2.modules.core.machine__TRASH.tabs__THRASH.TabMachineRecipes__THRASH.IMachineRecipe;
import lordfokas.stargatetech2.modules.enemy.TileParticleIonizer;
import lordfokas.stargatetech2.reference.BlockReference;
import lordfokas.stargatetech2.reference.TextureReference;
import lordfokas.stargatetech2.util.Helper;
import lordfokas.stargatetech2.util.api.ParticleIonizerRecipes;
import lordfokas.stargatetech2.util.api.ParticleIonizerRecipes.IonizerRecipe;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;

public class GUIParticleIonizer extends BaseGUI__OLD_AND_FLAWED {
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
			gui.drawSlot(TabColor.GREEN, x+2, y+2, 16, 16);
			gui.drawSlot(TabColor.GREEN, x+2, y+21, 16, 16);
			if(solid != null){
				gui.drawStack(solid, x+2, y+2);
				gui.drawLeft(solid.getDisplayName(), x + 20, y + 2, 0xAAAAAA);
			}
			FluidStack fluid = recipe.getFluid();
			if(fluid != null){
				gui.drawIcon(x+2, y + 21, fluid.getFluid().getStillIcon(), TextureMap.locationBlocksTexture, 16);
				gui.drawLeft(fluid.getFluid().getLocalizedName(), x + 20, y + 30, 0xAAAAAA);
			}
			gui.drawLeft(Helper.prettyNumber(recipe.ions * recipe.time) + " mB", x + 25, y + 16, 0xEEEEEE);
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
		super.addTab(new TabMachineRecipes__THRASH(tabRecipes));
		super.addTab(new TabConfiguration__THRASH(ionizer));
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
				itemRender.renderItemAndEffectIntoGUI(fontRendererObj, mc.renderEngine, solid, 151, 30);
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