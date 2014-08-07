package lordfokas.stargatetech2.factory.util;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import lordfokas.stargatetech2.core.machine.Inventory;

public class BufferItem extends Buffer<BufferItem, IInventory> {
	private Inventory inventory;
	
	public BufferItem(int slots) {
		super(BufferType.ITEM);
		inventory = new Inventory(slots);
	}

	@Override
	public void transferTo(BufferItem buffer) {
		for(int i = 0; i < inventory.getSizeInventory(); i++){
			ItemStack stack = inventory.getStackInSlot(i);
			if(stack != null){
				for(int j = 0; j < buffer.inventory.getSizeInventory(); j++){
					ItemStack target = buffer.inventory.getStackInSlot(j);
					if(target != null){
						if(target.isItemStackDamageable()) continue;
						if(target.isItemEnchantable()) continue;
						if(target.isItemEqual(stack)){
							int mov = target.getMaxStackSize() - target.stackSize;
							mov = mov > stack.stackSize ? stack.stackSize : mov;
							target.stackSize += mov;
							stack.stackSize -= mov;
						}
						if(stack.stackSize == 0){
							inventory.setInventorySlotContents(i, null);
							stack = null;
							break;
						}
					}
				}
			}
			if(stack != null){
				for(int j = 0; j < buffer.inventory.getSizeInventory(); j++){
					ItemStack target = buffer.inventory.getStackInSlot(j);
					if(target == null){
						buffer.inventory.setInventorySlotContents(j, stack);
						inventory.setInventorySlotContents(i, null);
						stack = null;
						break;
					}
				}
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		inventory.writeToNBT(nbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		inventory.readFromNBT(nbt);
	}

	@Override
	public float getFill() {
		int size = inventory.getSizeInventory();
		int full = 0;
		for(int i = 0; i < size; i++){
			if(inventory.getStackInSlot(i) != null) full++; 
		}
		return ((float)full) / ((float)size);
	}

	@Override
	public IInventory getContainer() {
		return inventory;
	}

}
