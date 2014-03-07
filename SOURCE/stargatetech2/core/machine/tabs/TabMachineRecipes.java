package stargatetech2.core.machine.tabs;

import net.minecraft.item.ItemStack;
import stargatetech2.core.base.BaseTab;
import stargatetech2.core.util.Stacks;

public class TabMachineRecipes extends BaseTab{

	public TabMachineRecipes() {
		super("Recipes", Stacks.book, TabColor.YELLOW);
	}

	@Override
	public int getSizeY() {
		return 80;
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
	}
}