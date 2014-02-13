package stargatetech2.automation;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import stargatetech2.IContentModule;
import stargatetech2.StargateTech2;
import stargatetech2.automation.block.BlockBusCable;
import stargatetech2.core.util.Stacks;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModuleAutomation implements IContentModule {
	public static BlockBusCable busCable;
	
	@Override
	public void preInit(){
		busCable = new BlockBusCable();
	}

	@Override
	public void init(){
		busCable.registerBlock();
	}

	@Override
	public void postInit(){
		GameRegistry.addShapedRecipe(new ItemStack(busCable, 8), "WWW", "NNN", "WWW", 'N', Stacks.naqIngot, 'W', new ItemStack(Block.cloth, 1, OreDictionary.WILDCARD_VALUE));
		StargateTech2.proxy.registerRenderers(Module.AUTOMATION);
	}

	@Override public void onServerStart(){}
	@Override public void onServerStop(){}

	@Override
	public String getModuleName(){
		return "Automation";
	}
}