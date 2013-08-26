package stargatetech2.core.util;

import java.util.ArrayList;

import net.minecraft.item.Item;
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
		
		public boolean equals(Object o){
			if(o instanceof Recipe){
				ItemStack s = ((Recipe)o).stack;
				return s.itemID == stack.itemID && s.getItemDamage() == stack.getItemDamage();
			}
			return false;
		}
	}
	
	private static ArrayList<Recipe> recipes = new ArrayList<Recipe>();
	
	public static Recipe getRecipe(ItemStack stack){
		if(stack == null) return null;
		for(Recipe recipe : recipes){
			if(recipe.stack.itemID == stack.itemID && recipe.stack.getItemDamage() == stack.getItemDamage()){
				return recipe.deepClone();
			}
		}
		return null;
	}
	
	public static void addRecipe(ItemStack stack, int ticks, int ions, int power){
		Recipe recipe = new Recipe(stack, ticks, ions, power);
		if(!recipes.contains(recipe)) recipes.add(recipe);
	}
	
	static{
		addRecipe(new ItemStack(Item.glowstone), 300, 5, 2);
	}
}