package stargatetech2.core.item;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import stargatetech2.api.ITabletAccess;
import stargatetech2.common.base.BaseItem;
import stargatetech2.common.reference.ItemReference;

public class ItemTabletPC extends BaseItem {

	public ItemTabletPC() {
		super(ItemReference.TABLET_PC);
		this.setCreativeTab(CreativeTabs.tabTools);
	}
	
	@Override
	public boolean onItemUseFirst(ItemStack s, EntityPlayer p, World w, int x, int y, int z, int d, float a, float b, float c){
		Block block = Block.blocksList[w.getBlockId(x, y, z)];
		if(block instanceof ITabletAccess){
			return ((ITabletAccess)block).onTabletAccess(p, w, x, y, z);
		}
		return false;
    }
}