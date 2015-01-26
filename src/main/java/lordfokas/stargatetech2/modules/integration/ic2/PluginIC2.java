package lordfokas.stargatetech2.modules.integration.ic2;

import ic2.api.item.IC2Items;
import lordfokas.stargatetech2.modules.integration.IPlugin;
import lordfokas.stargatetech2.util.api.ParticleIonizerRecipes;
import lordfokas.stargatetech2.util.api.ParticleIonizerRecipes.IonizerRecipe;
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
}
