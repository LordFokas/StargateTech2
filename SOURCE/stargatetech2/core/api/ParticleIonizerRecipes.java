package stargatetech2.core.api;

import java.util.ArrayList;

import net.minecraft.block.Block;
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
	
	public static void addRecipe(ItemStack stack, int seconds, int ions, int power){
		if(stack == null) return;
		Recipe recipe = new Recipe(stack, seconds * 20, ions, power);
		if(!recipes.contains(recipe)) recipes.add(recipe);
	}
	
	public static int getRecipeID(Recipe recipe){
		return recipes.indexOf(recipe);
	}
	
	public static Recipe getRecipe(int recipeID){
		return recipes.get(recipeID);
	}
	
	static{
		addRecipe(new ItemStack(Item.glowstone), 300, 5, 10);
		addRecipe(new ItemStack(Item.redstone), 150, 1, 5);
		addRecipe(new ItemStack(Block.blockRedstone), 1350, 1, 5);
	}
}