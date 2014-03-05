package stargatetech2.core.api;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public final class ParticleIonizerRecipes{
	private static final ParticleIonizerRecipes RECIPES = new ParticleIonizerRecipes();
	
	private ParticleIonizerRecipes(){}
	
	public static ParticleIonizerRecipes recipes(){
		return RECIPES;
	}
	
	public static final class IonizerRecipe{
		private FluidStack fluidIonizable;
		private ItemStack  solidIonizable;
		private int time, power, ions;
		
		public IonizerRecipe(FluidStack fluid, int time, int power, int ions){
			this(fluid, null, time, power, ions);
		}
		
		public IonizerRecipe(ItemStack solid, int time, int power, int ions){
			this(null, solid, time, power, ions);
		}
		
		public IonizerRecipe(FluidStack fluid, ItemStack solid, int time, int power, int ions){
			if(fluid == null && solid == null){
				throw new IllegalArgumentException("An Ionizer Recipe must have at least one valid ionizable material!");
			}
			if(time <= 0 || power <= 0 || ions <= 0){
				throw new IllegalArgumentException("An Ionizer Recipe's time, power and ions value MUST be positive!");
			}
			this.fluidIonizable = fluid;
			this.solidIonizable = solid;
			this.power = power;
			this.time = time;
			this.ions = ions;
		}
	}
	
	private ArrayList<IonizerRecipe> recipes = new ArrayList();
	
	public void addRecipe(IonizerRecipe recipe){
		// TODO: check if there's already a recipe with the same materials.
		recipes.add(recipe);
	}
}