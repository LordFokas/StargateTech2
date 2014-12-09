package lordfokas.stargatetech2.integration.plugins.ic2;

import ic2.api.item.IC2Items;
import lordfokas.stargatetech2.core.api.ParticleIonizerRecipes;
import lordfokas.stargatetech2.core.api.ParticleIonizerRecipes.IonizerRecipe;
import lordfokas.stargatetech2.integration.plugins.IPlugin;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class PluginIC2 implements IPlugin{
	public static ItemStack scrap;
	public static Fluid uuMatter;
	
	@Override public void load(){}
	
	@Override
	public void postload(){
		scrap = IC2Items.getItem("scrap");
		uuMatter = FluidRegistry.getFluid("ic2uumatter");
		
		IonizerRecipe scrapRecipe = new IonizerRecipe(scrap, 1800, 25, 250);
		IonizerRecipe uuRecipe = new IonizerRecipe(new FluidStack(uuMatter, 100), 6000, 1, 500);
		
		ParticleIonizerRecipes.recipes().addRecipe(scrapRecipe);
		ParticleIonizerRecipes.recipes().addRecipe(uuRecipe);
	}

	@Override public void fallback(){}
}
