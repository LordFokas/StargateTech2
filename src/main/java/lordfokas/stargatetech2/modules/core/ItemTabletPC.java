package lordfokas.stargatetech2.modules.core;

import lordfokas.naquadria.item.BaseItem;
import lordfokas.stargatetech2.api.ITabletAccess;
import lordfokas.stargatetech2.modules.core.ItemNaquadah.Type;
import lordfokas.stargatetech2.reference.ItemReference;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class ItemTabletPC extends BaseItem {

	public ItemTabletPC() {
		super(ItemReference.TABLET_PC);
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("stargatetech2:tablet_pc", "inventory"));
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack s, EntityPlayer p, World w, BlockPos pos, EnumHand h, EnumFacing f, float x, float y, float z){
		Block block = w.getBlockState(pos).getBlock();
		if(block instanceof ITabletAccess){
			return ((ITabletAccess)block).onTabletAccess(p, w, pos) ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
		}
		return EnumActionResult.PASS;
	}
}