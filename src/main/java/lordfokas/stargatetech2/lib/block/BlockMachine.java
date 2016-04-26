package lordfokas.stargatetech2.lib.block;

import lordfokas.stargatetech2.StargateTech2;
import lordfokas.stargatetech2.lib.tileentity.TileEntityMachine;
import lordfokas.stargatetech2.lib.util.TileEntityHelper;
import lordfokas.stargatetech2.util.GUIHandler.Screen;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
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
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block b) {
		if(useRedstoneSignal){
			checkRS(world, pos);
		}
	}
	
	private void checkRS(World world, BlockPos pos) {
		TileEntityMachine machine = TileEntityHelper.getTileEntityAs(world, pos, TileEntityMachine.class);
		int level = world.getStrongPower(pos);
		machine.setPowered(level > 0);
	}
	
	@Override
	public boolean onBlockActivated(World w, BlockPos pos, IBlockState state, EntityPlayer p, EnumFacing f, float hx, float hy, float hz) {
		ItemStack hand = p.getCurrentEquippedItem();
		TileEntityMachine machine = TileEntityHelper.getTileEntityAs(w, pos, TileEntityMachine.class);
		/*if(hand != null && hand.getItem() instanceof IToolWrench){ // FIXME re-enable when wrench interface is available
			if(p.isSneaking()){
				super.dropSelf(w, pos);
			}else{
				machine.rotateBlock();
			}
		}else*/ if(!p.isSneaking() && screen != null){
			p.openGui(StargateTech2.instance, screen.ordinal(), w, pos.getX(), pos.getY(), pos.getZ());
		}
		return super.onBlockActivated(w, pos, state, p, f, hx, hy, hz);
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