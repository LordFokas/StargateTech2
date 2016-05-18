package lordfokas.naquadria.tileentity.component.inventory;

import lordfokas.naquadria.tileentity.component.CapabilityComponent;
import lordfokas.naquadria.tileentity.component.IFilter;
import lordfokas.naquadria.tileentity.facing.FaceColor;
import lordfokas.naquadria.tileentity.facing.FaceColorFilter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.items.IItemHandler;

public class InventoryComponent extends CapabilityComponent<IItemHandler>{
	@CapabilityInject(IItemHandler.class)
	private static Capability<IItemHandler> itemHandlerCapability = null;
	protected final IFilter<FaceColor> filter;
	protected final Inventory inventory;
	
	public InventoryComponent(int size){
		this(size, FaceColorFilter.ANY);
	}
	
	public InventoryComponent(int size, IFilter<FaceColor> filter){
		this.inventory = new Inventory(size);
		this.filter = filter;
	}
	
	public Inventory getInventory(){
		return inventory;
	}
	
	@Override
	public Capability<IItemHandler> getCapability(){
		return itemHandlerCapability;
	}
	
	@Override
	public IItemHandler getCapability(EnumFacing side) {
		return filter.matches(getColor(side)) ? inventory : null;
	}
	
	@Override
	public NBTTagCompound serializeNBT(){
		return inventory.serializeNBT();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt){
		inventory.deserializeNBT(nbt);
	}
	
	public static class Advanced extends InventoryComponent{
		private final IItemHandler input, output;
		private final IFilter<FaceColor> outFilter;
		
		public Advanced(int size, IFilter<FaceColor> input, IFilter<FaceColor> output){
			super(size, input);
			this.outFilter = output;
			this.input = new Handler(inventory, true);
			this.output = new Handler(inventory, false);
		}

		@Override
		public IItemHandler getCapability(EnumFacing side){
			FaceColor color = getColor(side);
			if(filter.matches(color)) return input;
			if(outFilter.matches(color)) return output;
			return null;
		}
	}
	
	private static class Handler implements IItemHandler{
		private final Inventory inventory;
		private final boolean isInput;
		
		public Handler(Inventory inventory, boolean isInput){
			this.inventory = inventory;
			this.isInput = isInput;
		}
		
		@Override
		public int getSlots(){
			return inventory.getSlots();
		}

		@Override
		public ItemStack getStackInSlot(int slot){
			return inventory.getStackInSlot(slot);
		}

		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate){
			return isInput ? inventory.insertItem(slot, stack, simulate) : stack;
		}

		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate){
			return isInput ? null : inventory.extractItem(slot, amount, simulate);
		}
	}
}