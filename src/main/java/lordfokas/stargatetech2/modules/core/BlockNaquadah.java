package lordfokas.stargatetech2.modules.core;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import lordfokas.naquadria.block.BaseBlock;
import lordfokas.naquadria.render.IVariantProvider;
import lordfokas.stargatetech2.reference.BlockReference;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockNaquadah extends BaseBlock implements IVariantProvider{
	public static final IProperty<Type> TYPE = PropertyEnum.create("subtype", Type.class);
	
	public static enum Type implements IStringSerializable{
		ORE, BLOCK;
		
		@Override public String getName(){ return toString().toLowerCase(); }
	}
	
	public final IBlockState ore, block;
	
	public BlockNaquadah(){
		super(BlockReference.NAQUADAH, true, Material.ROCK);
		setHardness(3.0F);
		setResistance(5.0F);
		setHarvestLevel("pickaxe", 2);
		ore = blockState.getBaseState().withProperty(TYPE, Type.ORE);
		block = blockState.getBaseState().withProperty(TYPE, Type.BLOCK);
		setDefaultState(block);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, TYPE);
	}
	
	@Override
	public void getSubBlocks(Item i, CreativeTabs tab, List list){
		list.add(new ItemStack(this, 1, Type.ORE.ordinal()));
		list.add(new ItemStack(this, 1, Type.BLOCK.ordinal()));
	}
	
	@Override
	public int damageDropped(IBlockState state){
		return getMetaFromState(state);
	}
	
	@Override
	public void onBlockPlacedBy(World w, BlockPos pos, IBlockState state, EntityLivingBase e, ItemStack stack){
		w.setBlockState(pos, getStateFromMeta(stack.getMetadata()));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(TYPE).ordinal();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		IBlockState state = blockState.getBaseState();
		return state.withProperty(TYPE, Type.values()[meta]);
	}

	@Override
	public void addVariants(List<Pair<Integer, String>> variants) {
		for(Type t : Type.values())
			variants.add(Pair.of(t.ordinal(), "subtype=" + t.getName()));
	}
}