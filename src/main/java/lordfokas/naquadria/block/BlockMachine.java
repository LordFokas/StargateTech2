package lordfokas.naquadria.block;

import cofh.api.item.IToolHammer;
import lordfokas.naquadria.tileentity.TileEntityHelper;
import lordfokas.naquadria.tileentity.TileEntityMachine;
import lordfokas.stargatetech2.StargateTech2;
import lordfokas.stargatetech2.util.GUIHandler.Screen;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMachine extends BaseBlock implements ITileEntityProvider{
	private Class<? extends TileEntityMachine> tile;
	private boolean useRedstoneSignal = false;
	private Screen screen;
	
	public BlockMachine(String uName, Class<? extends TileEntityMachine> tile, Screen screen) {
		super(uName, true, true);
		this.screen = screen;
		this.tile = tile;
	}
	
	public void useRedstoneSignal(){
		useRedstoneSignal = true;
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
		super.onBlockPlacedBy(world, pos, state, entity, stack);
		if(entity instanceof EntityPlayerMP){
			TileEntityMachine machine = TileEntityHelper.getTileEntityAs(world, pos, TileEntityMachine.class);
			machine.setFacingFrom(entity);
		}
		if(useRedstoneSignal){
			checkRS(world, pos);
		}
	}
	
	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
		if(useRedstoneSignal){
			checkRS(world, pos);
		}
	}
	
	private void checkRS(IBlockAccess world, BlockPos pos) {
		TileEntityMachine machine = TileEntityHelper.getTileEntityAs(world, pos, TileEntityMachine.class);
		machine.setPowered(getMaxPower(world, pos) > 0);
	}
	
	private int getMaxPower(IBlockAccess world, BlockPos pos){
		int max = 0;
		for(EnumFacing side : EnumFacing.values()){
			int power = world.getStrongPower(pos, side);
			if(power > max) max = power;
		}
		return max;
	}
	
	@Override
	public boolean onBlockActivated(World w, BlockPos pos, IBlockState bs, EntityPlayer p, EnumHand h, ItemStack heldItem, EnumFacing s, float hX, float hY, float hZ) {
		TileEntityMachine machine = TileEntityHelper.getTileEntityAs(w, pos, TileEntityMachine.class);
		if(heldItem != null && heldItem.getItem() instanceof IToolHammer){
			if(p.isSneaking()){
				super.dropSelf(w, pos);
			}else{
				machine.rotateBlock(null); // The EnumFacing is unused internally
			}
		}else if(!p.isSneaking() && screen != null){
			p.openGui(StargateTech2.instance, screen.ordinal(), w, pos.getX(), pos.getY(), pos.getZ());
		}
		return super.onBlockActivated(w, pos, bs, p, h, heldItem, s, hX, hY, hZ);
	}
	
	@Override
	public TileEntityMachine createNewTileEntity(World world, int metadata) {
		try {
			return tile.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Error creating instance of Machine TileEntity", e);
		}
	}
}