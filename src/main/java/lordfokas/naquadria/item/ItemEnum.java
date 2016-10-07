package lordfokas.naquadria.item;

import lordfokas.naquadria.render.IVariantProvider;
import lordfokas.stargatetech2.reference.ModReference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Locale;

public class ItemEnum<T extends Enum<T> & IEnumItem> extends Item implements IVariantProvider {

    private final T[] types;
    private final String variantBase;

    public ItemEnum(Class<T> enumClass, String variantBase) {
        super();

        this.types = enumClass.getEnumConstants();
        this.variantBase = variantBase;

        setHasSubtypes(types.length > 1);
        setUnlocalizedName(ModReference.MOD_ID);
    }

    public ItemEnum(Class<T> enumClass) {
        this(enumClass, "type");
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return getItemType(stack).onItemUse(stack, player, world, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        return getItemType(stack).onItemRightClick(stack, world, player, hand);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + "." + getItemType(stack).toString().toLowerCase(Locale.ENGLISH);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        for (T type : types)
            subItems.add(new ItemStack(this, 1, type.ordinal()));
    }

    public T getItemType(ItemStack stack) {
        return types[MathHelper.clamp_int(stack.getItemDamage(), 0, types.length)];
    }

    public ItemStack asStack(T type, int amount) {
        return new ItemStack(this, amount, type.ordinal());
    }

    @Override
    public void addVariants(List<Pair<Integer, String>> variants) {
        for (T type : types)
            variants.add(Pair.of(type.ordinal(), variantBase + "=" + type.getSimpleName()));
    }
}
