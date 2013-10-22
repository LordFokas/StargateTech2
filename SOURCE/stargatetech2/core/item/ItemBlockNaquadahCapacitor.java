package stargatetech2.core.item;

import java.util.List;

import stargatetech2.core.tileentity.TileNaquadahCapacitor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ItemBlockNaquadahCapacitor extends ItemBlock {

	public ItemBlockNaquadahCapacitor(int id) {
		super(id);
		this.setMaxStackSize(1);
	}
	
	@Override
	public boolean placeBlockAt(ItemStack i, EntityPlayer p, World w, int x, int y, int z, int s, float hX, float hY, float hZ, int m){
		if(super.placeBlockAt(i, p, w, x, y, z, s, hX, hY, hZ, m)){
			NBTTagCompound nbt = i.getTagCompound();
			TileEntity te = w.getBlockTileEntity(x, y, z);
			if(te instanceof TileNaquadahCapacitor && nbt != null){
				w.setBlockMetadataWithNotify(x, y, z, nbt.getInteger("tier"), 3);
				te.readFromNBT(nbt);
				te.xCoord = x;
				te.yCoord = y;
				te.zCoord = z;
			}
			return true;
		}
		return false;
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean ignored){
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt == null) return;
		nbt = nbt.getCompoundTag("data");
		if(nbt == null) return;
		list.add("Tier " + nbt.getString("tier"));
		list.add(nbt.getString("power"));
	}
}