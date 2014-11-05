package lordfokas.stargatetech2.world.block;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import lordfokas.stargatetech2.core.base.BaseBlock;
import lordfokas.stargatetech2.core.reference.BlockReference;
import lordfokas.stargatetech2.core.reference.TextureReference;
import lordfokas.stargatetech2.core.util.Color;
import lordfokas.stargatetech2.core.util.IconRegistry;
import lordfokas.stargatetech2.world.item.ItemBlockLanteanWall;
import lordfokas.stargatetech2.world.rendering.RenderLanteanWall;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockLanteanWall extends BaseBlock {
	
	public BlockLanteanWall() {
		super(BlockReference.LANTEAN_WALL, true, true);
		setHarvestLevel("pickaxe", 2);
		setHardness(1.5F);
		setResistance(15F);
	}
	
	@Override
	public int getRenderType(){
		return RenderLanteanWall.instance().getRenderId();
	}
	
	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List list){
		for(int i = 0; i < 16; i++){
			list.add(new ItemStack(this, 1, i));
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
	public IIcon getBaseIcon(int side, int meta){
		return side < 2 ? IconRegistry.blockIcons.get(TextureReference.LANTEAN_BLOCK_CLEAN) : blockIcon;
	}
	
	@Override
	protected void registerBlock(){
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