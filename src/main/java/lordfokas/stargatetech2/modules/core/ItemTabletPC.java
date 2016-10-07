package lordfokas.stargatetech2.modules.core;

import lordfokas.naquadria.item.BaseItem;
import lordfokas.naquadria.render.IVariantProvider;
import lordfokas.stargatetech2.api.ITabletAccess;
import lordfokas.stargatetech2.reference.ItemReference;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class ItemTabletPC extends BaseItem implements IVariantProvider {

	public ItemTabletPC() {
		super(ItemReference.TABLET_PC);
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack s, EntityPlayer p, World w, BlockPos pos, EnumHand h, EnumFacing f, float x, float y, float z){
		Block block = w.getBlockState(pos).getBlock();
		if(block instanceof ITabletAccess){
			return ((ITabletAccess)block).onTabletAccess(p, w, pos) ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
		}
		return EnumActionResult.PASS;
	}

    @Override
    public void addVariants(List<Pair<Integer, String>> variants) {
        variants.add(Pair.of(0, "inventory"));
    }
}