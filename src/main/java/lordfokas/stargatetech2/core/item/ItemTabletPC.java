package lordfokas.stargatetech2.core.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import lordfokas.stargatetech2.api.ITabletAccess;
import lordfokas.stargatetech2.core.base.BaseItem;
import lordfokas.stargatetech2.core.reference.ItemReference;

public class ItemTabletPC extends BaseItem {

	public ItemTabletPC() {
		super(ItemReference.TABLET_PC);
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