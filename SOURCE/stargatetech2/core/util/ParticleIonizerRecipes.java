package stargatetech2.core.util;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

public class ParticleIonizerRecipes{
	public static class Recipe{
		public ItemStack stack;
		public int ticks, ions, power;
		
		public Recipe(ItemStack s, int t, int i, int p){
			stack = s;
			ticks = t;
			power = p;
			ions = i;
		}
		
		public Recipe deepClone(){
			return new Recipe(stack.copy(), ticks, ions, power);
		}
	}
	
	private static ArrayList<Recipe> recipes = new ArrayList<Recipe>();
	
	public static Recipe getRecipe(ItemStack stack){
		for(Recipe recipe : recipes){
			if(recipe.stack.itemID == stack.itemID && recipe.stack.getItemDamage() == stack.getItemDamage()){
				return recipe.deepClone();
			}
		}
		return null;
	}
}