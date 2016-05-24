package lordfokas.stargatetech2.modules.core;

import lordfokas.naquadria.item.BaseItem;
import lordfokas.stargatetech2.api.ITabletAccess;
import lordfokas.stargatetech2.reference.ItemReference;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemTabletPC extends BaseItem {

	public ItemTabletPC() {
		super(ItemReference.TABLET_PC);
	}
	
	@Override
	public boolean onItemUseFirst(ItemStack s, EntityPlayer p, World w, BlockPos pos, EnumFacing side, float a, float b, float c){
		Block block = w.getBlockState(pos).getBlock();
		if(block instanceof ITabletAccess){
			return ((ITabletAccess)block).onTabletAccess(p, w, pos);
		}
		return false;
    }
}