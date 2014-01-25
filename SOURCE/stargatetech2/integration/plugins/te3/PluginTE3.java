package stargatetech2.integration.plugins.te3;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import stargatetech2.common.reference.ConfigReference;
import stargatetech2.core.ModuleCore;
import stargatetech2.core.item.ItemNaquadah;
import stargatetech2.integration.plugins.BasePlugin;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.registry.GameRegistry;

public class PluginTE3 extends BasePlugin {

	public PluginTE3() {
		super("ThermalExpansion", ConfigReference.KEY_PLUGINS_TE3);
	}

	@Override
	protected void load() {
		addPulverizerRecipe(4000, new ItemStack(ModuleCore.naquadahOre), new ItemStack(ModuleCore.naquadah, 2, ItemNaquadah.DUST.ID));
		addPulverizerRecipe(2400, new ItemStack(ModuleCore.naquadah, 1, ItemNaquadah.INGOT.ID), new ItemStack(ModuleCore.naquadah, 1, ItemNaquadah.DUST.ID));
	}

	@Override
	protected void fallback() {
		GameRegistry.addShapelessRecipe(new ItemStack(ModuleCore.naquadah, 1, ItemNaquadah.DUST.ID), new ItemStack(ModuleCore.naquadahOre));
	}
	
	private void addPulverizerRecipe(int energy, ItemStack input, ItemStack output){
		addPulverizerRecipe(energy, input, output, null, 0);
	}
	
	private void addPulverizerRecipe(int energy, ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput, int secondaryChance){
		NBTTagCompound toSend = new NBTTagCompound();
		
		toSend.setInteger("energy", energy);
		toSend.setCompoundTag("input", new NBTTagCompound());
		toSend.setCompoundTag("primaryOutput", new NBTTagCompound());
		
		input.writeToNBT(toSend.getCompoundTag("input"));
		
		primaryOutput.writeToNBT(toSend.getCompoundTag("primaryOutput"));
		
		if(secondaryOutput != null && secondaryChance > 0){
			NBTTagCompound nbt = new NBTTagCompound();
			secondaryOutput.writeToNBT(nbt);
			toSend.setCompoundTag("secondaryOutput", nbt);
			toSend.setInteger("secondaryChance", secondaryChance);
		}
		
		FMLInterModComms.sendMessage("ThermalExpansion", "PulverizerRecipe", toSend);
	}
}