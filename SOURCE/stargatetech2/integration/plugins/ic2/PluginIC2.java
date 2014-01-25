package stargatetech2.integration.plugins.ic2;

import ic2.api.item.Items;
import net.minecraft.item.ItemStack;
import stargatetech2.common.reference.ConfigReference;
import stargatetech2.core.util.ParticleIonizerRecipes;
import stargatetech2.integration.plugins.BasePlugin;

public class PluginIC2 extends BasePlugin {
	public static ItemStack scrap;
	public static ItemStack scrapBox;
	public static ItemStack uuMatter;
	
	public PluginIC2(){
		super("IC2", ConfigReference.KEY_PLUGINS_IC2);
	}

	@Override
	protected void load(){
		scrap = Items.getItem("scrap");
		scrapBox = Items.getItem("scrapBox");
		uuMatter = Items.getItem("matter");
		
		ParticleIonizerRecipes.addRecipe(scrap, 200, 8, 4);
		ParticleIonizerRecipes.addRecipe(scrapBox, 1800, 8, 4);
		ParticleIonizerRecipes.addRecipe(uuMatter, 1800, 40, 1);
	}

	@Override protected void fallback(){}
}