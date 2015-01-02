package lordfokas.stargatetech2.core;

import java.util.List;

import lordfokas.stargatetech2.core.base.BaseBlock;
import lordfokas.stargatetech2.core.reference.BlockReference;
import lordfokas.stargatetech2.core.reference.TextureReference;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;

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
	public int getRenderType(){
		return RenderNaquadah.instance().getRenderId();
	}
	
	@Override
	public void getSubBlocks(Item i, CreativeTabs tab, List list){
		list.add(new ItemStack(this, 1, ORE));
		list.add(new ItemStack(this, 1, BLOCK));
	}
	
	@Override
	public IIcon getBaseIcon(int side, int meta){
		return meta != 0 ? blockIcon : IconRegistry.blockIcons.get(TextureReference.NAQUADAH_ORE);
	}
	
	@Override
	public int damageDropped(int meta){
		return meta;
	}
	
	@Override
	public void onBlockPlacedBy(World w, int x, int y, int z, EntityLivingBase e, ItemStack stack){
		w.setBlockMetadataWithNotify(x, y, z, stack.getItemDamage(), 2);
	}
	
	@Override
	protected void registerBlock(){
		GameRegistry.registerBlock(this, ItemBlockNaquadah.class, getUnlocalizedName());
	}
}