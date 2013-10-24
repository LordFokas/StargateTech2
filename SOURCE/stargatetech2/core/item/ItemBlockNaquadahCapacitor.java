package stargatetech2.core.item;

import java.util.List;

import stargatetech2.core.tileentity.TileNaquadahCapacitor;
import net.minecraft.creativetab.CreativeTabs;
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
		this.setHasSubtypes(true);
	}
	
	@Override
	public boolean placeBlockAt(ItemStack i, EntityPlayer p, World w, int x, int y, int z, int s, float hX, float hY, float hZ, int m){
		if(super.placeBlockAt(i, p, w, x, y, z, s, hX, hY, hZ, m)){
			NBTTagCompound nbt = i.getTagCompound();
			TileEntity te = w.getBlockTileEntity(x, y, z);
			if(te instanceof TileNaquadahCapacitor){
				te.setWorldObj(w);
				if(nbt != null){
					te.readFromNBT(nbt);
					te.xCoord = x;
					te.yCoord = y;
					te.zCoord = z;
				}else{
					((TileNaquadahCapacitor)te).upgrade(i.getItemDamage());
				}
			}
			return true;
		}
		return false;
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean ignored){
		String power = null;
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt != null) nbt = nbt.getCompoundTag("data");
		if(nbt != null) power = nbt.getString("power");
		int tier = stack.getItemDamage();
		
		list.add("Tier " + TileNaquadahCapacitor.TIERS[tier].name);
		if(power != null){
			list.add(power);
		}else{
			list.add("0 / " + (int) TileNaquadahCapacitor.TIERS[tier].maxPower);
		}
	}
	
	@Override
	public void getSubItems(int id, CreativeTabs tab, List list){
		for(int i = 0; i < TileNaquadahCapacitor.TIERS.length; i++){
			list.add(new ItemStack(id, 1, i));
		}
	}
}