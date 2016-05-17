package lordfokas.naquadria.tileentity.component.inventory;

import lordfokas.naquadria.tileentity.component.CapabilityComponent;
import lordfokas.naquadria.tileentity.facing.FaceColor;
import lordfokas.naquadria.tileentity.facing.FaceColorFilter;
import lordfokas.naquadria.tileentity.facing.IFaceColorFilter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.items.IItemHandler;

public class InventoryComponent extends CapabilityComponent<IItemHandler>{
	@CapabilityInject(IItemHandler.class)
	private static Capability<IItemHandler> itemHandlerCapability = null;
	protected final IFaceColorFilter filter;
	protected final Inventory inventory;
	
	public InventoryComponent(int size){
		this(size, FaceColorFilter.ANY);
	}
	
	public InventoryComponent(int size, IFaceColorFilter filter){
		this.inventory = new Inventory(size);
		this.filter = filter;
	}
	
	public Inventory getInventory(){
		return inventory;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt){
		inventory.readFromNBT(nbt);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		return inventory.writeToNBT(nbt);
	}
	
	@Override
	public Capability<IItemHandler> getCapability(){
		return itemHandlerCapability;
	}
	
	@Override
	public IItemHandler getCapability(EnumFacing side) {
		return filter.doesColorMatch(getColor(side)) ? inventory : null;
	}
	
	public static class Advanced extends InventoryComponent{
		private final IItemHandler input, output;
		private final IFaceColorFilter outFilter;
		
		public Advanced(int size, IFaceColorFilter input, IFaceColorFilter output){
			super(size, input);
			this.outFilter = output;
			this.input = new Handler(inventory, true);
			this.output = new Handler(inventory, false);
		}

		@Override
		public IItemHandler getCapability(EnumFacing side){
			FaceColor color = getColor(side);
			if(filter.doesColorMatch(color)) return input;
			if(outFilter.doesColorMatch(color)) return output;
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