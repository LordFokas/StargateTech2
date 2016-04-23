package lordfokas.stargatetech2.lib.tileentity;

import lordfokas.stargatetech2.api.bus.IBusInterface;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

public class FakeInterfaces{
	public interface IFakeSyncBusDevice{
		public IBusInterface[] getInterfaces(int side);
		public World getWorld();
		public int getXCoord();
		public int getYCoord();
		public int getZCoord();
		public void setEnabled(boolean enabled);
		public boolean getEnabled();
		public void setAddress(short addr);
		public short getAddress();
	}
	
	public interface IFakeFluidHandler{
		public int fill(ForgeDirection from, FluidStack resource, boolean doFill);
		public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain);
		public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain);
		public boolean canFill(ForgeDirection from, Fluid fluid);
		public boolean canDrain(ForgeDirection from, Fluid fluid);
		public FluidTankInfo[] getTankInfo(ForgeDirection from);
	}
	
	public interface IFakeSidedInventory{
		public int getSizeInventory();
		public ItemStack getStackInSlot(int slot);
		public ItemStack decrStackSize(int slot, int amount);
		public ItemStack getStackInSlotOnClosing(int slot);
		public void setInventorySlotContents(int slot, ItemStack stack);
		public String getInventoryName();
		public boolean hasCustomInventoryName();
		public int getInventoryStackLimit();
		public void markDirty();
		public boolean isUseableByPlayer(EntityPlayer player);
		public void openInventory();
		public void closeInventory();
		public boolean isItemValidForSlot(int slot, ItemStack stack);
		public int[] getAccessibleSlotsFromSide(int side);
		public boolean canInsertItem(int slot, ItemStack stack, int side);
		public boolean canExtractItem(int slot, ItemStack stack, int side);
	}
	
	public interface IFakeEnergyHandler{
		public boolean canConnectEnergy(ForgeDirection from);
		public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate);
		public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate);
		public int getEnergyStored(ForgeDirection from);
		public int getMaxEnergyStored(ForgeDirection from);
	}
}
