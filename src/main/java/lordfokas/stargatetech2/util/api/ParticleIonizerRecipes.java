package lordfokas.stargatetech2.util.api;

import java.util.ArrayList;
import java.util.List;

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
		public final int time, power, ions;
		
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
			this.fluidIonizable = (fluid == null) ? null : fluid.copy();
			this.solidIonizable = (solid == null) ? null : solid.copy();
			if(solid != null){
				this.solidIonizable.stackSize = 1;
			}
			this.power = power;
			this.time = time;
			this.ions = ions;
		}
		
		public boolean checkMatch(FluidStack fs, ItemStack[] stacks){
			if(fluidIonizable != null){
				if(fs == null){
					return false;
				} else if(!fs.containsFluid(fluidIonizable)){
					return false;
				}
			}
			if(solidIonizable != null){
				for(ItemStack stack : stacks){
					if(stack == null) continue;
					stack = stack.copy();
					stack.stackSize = solidIonizable.stackSize;
					if(ItemStack.areItemStacksEqual(solidIonizable, stack)){
						return true;
					}
				}
				return false;
			}else{
				return true;
			}
		}
		
		public FluidStack getFluid(){
			return fluidIonizable == null ? null : fluidIonizable.copy();
		}
		
		public ItemStack getSolid(){
			return solidIonizable == null ? null : solidIonizable.copy();
		}
	}
	
	private ArrayList<IonizerRecipe> recipes = new ArrayList();
	
	public void addRecipe(IonizerRecipe recipe){
		for(IonizerRecipe existing : recipes){
			boolean solidExists, fluidExists;
			solidExists = ItemStack.areItemStacksEqual(existing.solidIonizable, recipe.solidIonizable);
			fluidExists = (existing.fluidIonizable == null) ? recipe.fluidIonizable == null : existing.fluidIonizable.isFluidEqual(recipe.fluidIonizable);
			if(solidExists && fluidExists){
				return;
			}
		}
		recipes.add(recipe);
	}
	
	public List<IonizerRecipe> getRecipes(){
		return recipes;
	}
	
	public boolean isIonizable(FluidStack fs){
		for(IonizerRecipe recipe : recipes){
			if(fs.isFluidEqual(recipe.fluidIonizable)){
				return true;
			}
		}
		return false;
	}
	
	public boolean isIonizable(ItemStack is){
		for(IonizerRecipe recipe : recipes){
			if(ItemStack.areItemStacksEqual(is, recipe.solidIonizable)){
				return true;
			}
		}
		return false;
	}
	
	public int getRecipeID(IonizerRecipe recipe){
		return recipes.indexOf(recipe);
	}
	
	public IonizerRecipe getRecipe(int recipe){
		if(recipe < 0 || recipe >= recipes.size()){
			return null;
		}else{
			return recipes.get(recipe);
		}
	}
}