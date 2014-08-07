package lordfokas.stargatetech2.integration.plugins.te3;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import lordfokas.stargatetech2.core.ModuleCore;
import lordfokas.stargatetech2.core.item.ItemNaquadah;
import lordfokas.stargatetech2.core.reference.ConfigReference;
import lordfokas.stargatetech2.core.util.Stacks;
import lordfokas.stargatetech2.integration.plugins.BasePlugin;
import cpw.mods.fml.common.event.FMLInterModComms;

public class PluginTE3 extends BasePlugin {

	public PluginTE3() {
		super("ThermalExpansion", ConfigReference.KEY_PLUGINS_TE3);
	}

	@Override
	protected void load() {
		addPulverizerRecipe(4000, new ItemStack(ModuleCore.naquadahBlock), new ItemStack(ModuleCore.naquadahItem, 2, ItemNaquadah.DUST.ID));
		addPulverizerRecipe(2400, new ItemStack(ModuleCore.naquadahItem, 1, ItemNaquadah.INGOT.ID), new ItemStack(ModuleCore.naquadahItem, 1, ItemNaquadah.DUST.ID));
		addSmelterRecipe(4800, Stacks.naqIngot, new ItemStack(Item.netherQuartz, 3), Stacks.circuit);
		addSmelterRecipe(3200, Stacks.naqDust, new ItemStack(Item.netherQuartz, 3), Stacks.circuit);
	}
	
	@Override
	protected void postLoad(){
		// TODO: re-enable in MC 1.7 when CoFH add list sync.
		// CoFHFriendHelper.init();
	}
	
	@Override protected void fallback(){}
	
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
	
	private void addSmelterRecipe(int energy, ItemStack primaryInput, ItemStack secondaryInput, ItemStack primaryOutput){
		addSmelterRecipe(energy, primaryInput, secondaryInput, primaryOutput, null, 0);
	}
	
	private void addSmelterRecipe(int energy, ItemStack primaryInput, ItemStack secondaryInput, ItemStack primaryOutput, ItemStack secondaryOutput, int secondaryChance) {
		NBTTagCompound toSend = new NBTTagCompound();

		toSend.setInteger("energy", energy);
		toSend.setCompoundTag("primaryInput", new NBTTagCompound());
		toSend.setCompoundTag("secondaryInput", new NBTTagCompound());
		toSend.setCompoundTag("primaryOutput", new NBTTagCompound());

		primaryInput.writeToNBT(toSend.getCompoundTag("primaryInput"));
		secondaryInput.writeToNBT(toSend.getCompoundTag("secondaryInput"));
		primaryOutput.writeToNBT(toSend.getCompoundTag("primaryOutput"));
		
		if(secondaryOutput != null && secondaryChance > 0){
			NBTTagCompound nbt = new NBTTagCompound();
			secondaryOutput.writeToNBT(nbt);
			toSend.setCompoundTag("secondaryOutput", nbt);
			toSend.setInteger("secondaryChance", secondaryChance);
		}

		FMLInterModComms.sendMessage("ThermalExpansion", "SmelterRecipe", toSend);
	}
}