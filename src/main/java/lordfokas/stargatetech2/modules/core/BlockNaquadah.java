package lordfokas.stargatetech2.modules.core;

import java.util.List;

import lordfokas.naquadria.block.BaseBlock;
import lordfokas.stargatetech2.reference.BlockReference;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockNaquadah extends BaseBlock {
	public static final int ORE = 0;
	public static final int BLOCK = 8;
	
	public BlockNaquadah(){
		super(BlockReference.NAQUADAH, true, Material.rock);
		this.setHardness(3.0F);
		this.setResistance(5.0F);
		setHarvestLevel("pickaxe", 2);
		
	}
	
	@Override
	public void getSubBlocks(Item i, CreativeTabs tab, List list){
		list.add(new ItemStack(this, 1, ORE));
		list.add(new ItemStack(this, 1, BLOCK));
	}
	
	/*@Override
	public IIcon getBaseIcon(int side, int meta){
		return meta != 0 ? blockIcon : IconRegistry.blockIcons.get(TextureReference.NAQUADAH_ORE);
	}*/
	
	@Override
	public int damageDropped(IBlockState state){
		return -0x8000; // FIXME dafuq does this do now?
	}
	
	@Override
	public void onBlockPlacedBy(World w, BlockPos pos, IBlockState state, EntityLivingBase e, ItemStack stack){
		w.setBlockMetadataWithNotify(pos, stack.getItemDamage(), 2);
	}
	
	@Override
	protected void registerBlock(){
		GameRegistry.registerBlock(this, ItemBlockNaquadah.class, getUnlocalizedName());
	}
}