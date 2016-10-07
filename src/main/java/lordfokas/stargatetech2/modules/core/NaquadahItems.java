package lordfokas.stargatetech2.modules.core;

import lordfokas.naquadria.item.IEnumItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Locale;

public enum NaquadahItems implements IEnumItem {
    NAQUADAH_INGOT(),
    NAQUADAH_DUST(),
    NAQUADAH_PLATE(),
    CIRCUIT_CRYSTAL(),
    COIL_NAQUADAH(),
    COIL_ENDER(),
    ;

    @Override
    public String getSimpleName() {
        return toString().toLowerCase(Locale.ENGLISH);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        return ActionResult.newResult(EnumActionResult.PASS, stack);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return EnumActionResult.PASS;
    }
}
