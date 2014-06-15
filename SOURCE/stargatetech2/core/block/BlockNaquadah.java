package stargatetech2.core.block;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import stargatetech2.core.base.BaseBlock;
import stargatetech2.core.item.ItemBlockNaquadah;
import stargatetech2.core.reference.BlockReference;
import stargatetech2.core.reference.TextureReference;
import stargatetech2.core.rendering.RenderNaquadah;
import stargatetech2.core.util.IconRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockNaquadah extends BaseBlock {
	public static final int ORE = 0;
	public static final int BLOCK = 8;
	
	public BlockNaquadah(){
		super(BlockReference.NAQUADAH, true, Material.rock);
		this.setHardness(3.0F);
		this.setResistance(5.0F);
		MinecraftForge.setBlockHarvestLevel(this, "pickaxe", 2);
	}
	
	@Override
	public int getRenderType(){
		return RenderNaquadah.instance().getRenderId();
	}
	
	@Override
	public void getSubBlocks(int id, CreativeTabs tab, List list){
		list.add(new ItemStack(id, 1, ORE));
		list.add(new ItemStack(id, 1, BLOCK));
	}
	
	@Override
	public Icon getBaseIcon(int side, int meta){
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
	public void registerBlock(){
		GameRegistry.registerBlock(this, ItemBlockNaquadah.class, getUnlocalizedName());
	}
}