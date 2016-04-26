package lordfokas.stargatetech2.ZZ_THRASH;

@Deprecated
public class TabMachineRecipes__THRASH /*extends BaseTab__OLD_AND_FLAWED*/{
	/*public static interface IMachineRecipe{
		public void renderAt(int x, int y);
	}
	
	private IMachineRecipe[] recipes;
	private int page = 1;
	private int numpages;
	
	public TabMachineRecipes__THRASH(IMachineRecipe[] recipes) {
		super("Recipes", Stacks.book, TabColor.GREEN);
		this.recipes = recipes;
		this.numpages = (recipes.length / 2) + (recipes.length % 2);
	}

	@Override
	public int getSizeY() {
		return 121;
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
			gui.drawDarkerArea(getColor(), 5, 31, 97, 39);
			recipe.renderAt(5, 31);
		}
		if(recipes.length > r + 1){
			IMachineRecipe recipe = recipes[r + 1];
			gui.drawDarkerArea(getColor(), 5, 72, 97, 39);
			recipe.renderAt(5, 72);
		}
	}
	
	@Override
	public boolean handleClick(int x, int y){
		if(elementHit(94, 21, x, y, 8, 8)){
			page++;
			if(page > numpages){
				page = 1;
			}
			gui.playClick(0.8F);
			return false;
		}
		if(elementHit(5, 21, x, y, 8, 8)){
			page--;
			if(page < 1){
				page = numpages;
			}
			gui.playClick(0.7F);
			return false;
		}
		return true;
	}*/
}