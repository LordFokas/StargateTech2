package lordfokas.stargatetech2.modules.integration.te4;

public class PluginTE4 /* implements IPlugin */{
	/*@Override
	public void load() {
		addPulverizerRecipe(4000, new ItemStack(ModuleCore.naquadahBlock), new ItemStack(ModuleCore.naquadahItem, 2, ItemNaquadah.DUST.ID));
		addPulverizerRecipe(2400, new ItemStack(ModuleCore.naquadahItem, 1, ItemNaquadah.INGOT.ID), new ItemStack(ModuleCore.naquadahItem, 1, ItemNaquadah.DUST.ID));
		addSmelterRecipe(4800, Stacks.naqIngot, new ItemStack(Items.quartz, 3), Stacks.circuit);
		addSmelterRecipe(3200, Stacks.naqDust, new ItemStack(Items.quartz, 3), Stacks.circuit);
	}
	
	@Override
	public void postload(){
		// TODO: re-enable in MC 1.7 when CoFH add list sync.
		// CoFHFriendHelper.init();
	}
	
	private void addPulverizerRecipe(int energy, ItemStack input, ItemStack output){
		addPulverizerRecipe(energy, input, output, null, 0);
	}
	
	private void addPulverizerRecipe(int energy, ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput, int secondaryChance){
		NBTTagCompound toSend = new NBTTagCompound();
		
		toSend.setInteger("energy", energy);
		toSend.setTag("input", new NBTTagCompound());
		toSend.setTag("primaryOutput", new NBTTagCompound());
		
		input.writeToNBT(toSend.getCompoundTag("input"));
		primaryOutput.writeToNBT(toSend.getCompoundTag("primaryOutput"));
		
		if(secondaryOutput != null && secondaryChance > 0){
			NBTTagCompound nbt = new NBTTagCompound();
			secondaryOutput.writeToNBT(nbt);
			toSend.setTag("secondaryOutput", nbt);
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
		toSend.setTag("primaryInput", new NBTTagCompound());
		toSend.setTag("secondaryInput", new NBTTagCompound());
		toSend.setTag("primaryOutput", new NBTTagCompound());

		primaryInput.writeToNBT(toSend.getCompoundTag("primaryInput"));
		secondaryInput.writeToNBT(toSend.getCompoundTag("secondaryInput"));
		primaryOutput.writeToNBT(toSend.getCompoundTag("primaryOutput"));
		
		if(secondaryOutput != null && secondaryChance > 0){
			NBTTagCompound nbt = new NBTTagCompound();
			secondaryOutput.writeToNBT(nbt);
			toSend.setTag("secondaryOutput", nbt);
			toSend.setInteger("secondaryChance", secondaryChance);
		}

		FMLInterModComms.sendMessage("ThermalExpansion", "SmelterRecipe", toSend);
	}*/
}
