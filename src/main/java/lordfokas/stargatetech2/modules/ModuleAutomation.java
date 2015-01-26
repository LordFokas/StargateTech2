package lordfokas.stargatetech2.modules;

import lordfokas.stargatetech2.StargateTech2;
import lordfokas.stargatetech2.modules.IContentModule.Module;
import lordfokas.stargatetech2.modules.automation.BlockBusCable;
import lordfokas.stargatetech2.util.Stacks;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.registry.GameRegistry;


public class ModuleAutomation implements IContentModule {
	public static BlockBusCable busCable;
	
	@Override
	public void preInit(){
		busCable = new BlockBusCable();
	}

	@Override public void init(){}

	@Override
	public void postInit(){
		GameRegistry.addShapedRecipe(new ItemStack(busCable, 32), "WWW", "NNN", "WWW", 'N', Stacks.naqIngot, 'W', new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE));
		StargateTech2.proxy.registerRenderers(Module.AUTOMATION);
	}

	@Override public void onServerStart(){}
	@Override public void onServerStop(){}

	@Override
	public String getModuleName(){
		return "Automation";
	}
}