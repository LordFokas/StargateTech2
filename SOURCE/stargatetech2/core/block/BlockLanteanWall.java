package stargatetech2.core.block;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import stargatetech2.common.base.BaseBlock;
import stargatetech2.common.reference.BlockReference;
import stargatetech2.common.reference.TextureReference;
import stargatetech2.common.util.Color;
import stargatetech2.common.util.IconRegistry;
import stargatetech2.core.item.ItemBlockLanteanWall;
import stargatetech2.core.rendering.RenderLanteanWall;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockLanteanWall extends BaseBlock {
	
	public BlockLanteanWall() {
		super(BlockReference.LANTEAN_WALL, true, true);
		MinecraftForge.setBlockHarvestLevel(this, "pickaxe", 2);
		setHardness(1.5F);
		setResistance(15F);
	}
	
	@Override
	public int getRenderType(){
		return RenderLanteanWall.instance().getRenderId();
	}
	
	@Override
	public void getSubBlocks(int id, CreativeTabs tab, List list){
		for(int i = 0; i < 16; i++){
			list.add(new ItemStack(id, 1, i));
		}
	}
	
	@Override
	public void onBlockPlacedBy(World w, int x, int y, int z, EntityLivingBase e, ItemStack stack){
		w.setBlockMetadataWithNotify(x, y, z, stack.getItemDamage(), 2);
	}
	
	public Color getColor(int metadata){
		return Color.COLORS[metadata];
	}
	
	@Override
	public Icon getBaseIcon(int side, int meta){
		return side < 2 ? IconRegistry.blockIcons.get(TextureReference.LANTEAN_BLOCK_CLEAN) : blockIcon;
	}
	
	@Override
	public void registerBlock(){
		GameRegistry.registerBlock(this, ItemBlockLanteanWall.class, getUnlocalizedName());
	}
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z){
		return new ItemStack(this, 1, world.getBlockMetadata(x, y, z));
	}
	
	@Override
	public int damageDropped(int meta){
		return meta;
	}
}