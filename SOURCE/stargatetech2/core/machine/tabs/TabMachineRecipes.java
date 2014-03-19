package stargatetech2.core.machine.tabs;

import stargatetech2.core.base.BaseGUI.Arrow;
import stargatetech2.core.base.BaseTab;
import stargatetech2.core.util.Stacks;

public class TabMachineRecipes extends BaseTab{
	public static interface IMachineRecipe{
		public void renderAt(int x, int y);
	}
	
	private IMachineRecipe[] recipes;
	private int page = 1;
	private int numpages;
	
	public TabMachineRecipes(IMachineRecipe[] recipes) {
		super("Recipes", Stacks.book, TabColor.GREEN);
		this.recipes = recipes;
		this.numpages = (recipes.length / 2) + (recipes.length % 2);
	}

	@Override
	public int getSizeY() {
		return 113;
	}

	@Override
	public void render() {
		gui.drawArrow(Arrow.LEFT, 5, 21);
		gui.drawArrow(Arrow.RIGHT, 94, 21);
		gui.drawCentered("Page " + page + " / " + numpages, 3 + getSizeX() / 2, 22, 0xCCCCCC);
		gui.drawCentered(recipes.length + " Recipes", 3 + getSizeX() / 2, getSizeY() - 7, 0xCCCCCC);
		int r = (page - 1) * 2;
		if(recipes.length > r){
			IMachineRecipe recipe = recipes[r];
			gui.drawDarkerArea(getColor(), 5, 31, 97, 35);
			recipe.renderAt(5, 31);
		}
		if(recipes.length > r + 1){
			IMachineRecipe recipe = recipes[r + 1];
			gui.drawDarkerArea(getColor(), 5, 68, 97, 35);
			recipe.renderAt(5, 68);
		}
	}
	
	@Override
	public boolean handleClick(int x, int y){
		if(hitButton(x, y, 94, 21)){
			page++;
			if(page > numpages){
				page = 1;
			}
			gui.playClick(0.8F);
			return false;
		}
		if(hitButton(x, y, 5, 21)){
			page--;
			if(page < 1){
				page = numpages;
			}
			gui.playClick(0.7F);
			return false;
		}
		return true;
	}
	
	private boolean hitButton(int cx, int cy, int bx, int by){
		return cx >= bx && cx < bx + 8 && cy >= by && cy < by + 8;
	}
}