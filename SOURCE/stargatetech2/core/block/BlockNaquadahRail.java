package stargatetech2.core.block;

import java.util.List;

import net.minecraft.block.BlockRailBase;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import stargatetech2.StargateTech2;
import stargatetech2.api.shields.IShieldable;
import stargatetech2.api.shields.ShieldPermissions;
import stargatetech2.common.reference.BlockReference;
import stargatetech2.common.reference.ModReference;
import stargatetech2.common.util.StargateTab;
import stargatetech2.common.util.Vec3Int;
import stargatetech2.core.rendering.RenderNaquadahRail;
import stargatetech2.core.tileentity.TileShield;
import stargatetech2.core.tileentity.TileShieldEmitter;
import buildcraft.api.tools.IToolWrench;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockNaquadahRail extends BlockRailBase implements IShieldable, ITileEntityProvider{
	public static int currentRenderPass = -1;
	private boolean useSuperClassRenderer = false;
	
	public BlockNaquadahRail() {
		super(StargateTech2.instance.config.getBlockID(BlockReference.NAQUADAH_RAIL), false);
		this.setUnlocalizedName(BlockReference.NAQUADAH_RAIL);
		this.func_111022_d(ModReference.MOD_ID + ":" + BlockReference.NAQUADAH_RAIL);
		this.setCreativeTab(StargateTab.instance);
		this.setBlockUnbreakable();
		this.setResistance(20000000F);
	}
	
	public void toggleRenderOverride(){
		useSuperClassRenderer = !useSuperClassRenderer;
	}
	
	@Override
	public int getRenderType(){
		if(useSuperClassRenderer)
			return super.getRenderType();
		else
			return RenderNaquadahRail.instance().getRenderId();
	}
	
	@Override
	public boolean canRenderInPass(int pass){
		currentRenderPass = pass;
		return true;
	}
	
	@Override
	public int getMobilityFlag(){
		return 2;
	}
	
	public void registerBlock(){
		GameRegistry.registerBlock(this, getUnlocalizedName());
	}
	
	@Override
	public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer p, int s, float hx, float hy, float hz){
		ItemStack stack = p.inventory.getCurrentItem();
		Item item = stack != null ? stack.getItem() : null;
		if(item instanceof IToolWrench){
			IToolWrench wrench = (IToolWrench) item;
			boolean isShielded = (w.getBlockMetadata(x,y,z) & 8) != 0;
			if(wrench.canWrench(p, x, y, z) && !isShielded){
				dropBlockAsItem(w, x, y, z, 0, 0);
				w.setBlock(x, y, z, 0, 0, 3);
				wrench.wrenchUsed(p, x, y, z);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean canMakeSlopes(World world, int x, int y, int z){
		return false;
	}
	
	@Override
	public boolean isFlexibleRail(World world, int y, int x, int z){
		return false;
	}
	
	@Override
	public int getBasicRailMetadata(IBlockAccess world, EntityMinecart cart, int x, int y, int z){
		return world.getBlockMetadata(x, y, z) & 7;
	}
	
	@Override
	public void onShield(World world, int x, int y, int z, int px, int py, int pz) {
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if(te instanceof TileShield){
			((TileShield)te).setEmitter(new Vec3Int(px, py, pz));
			int meta = world.getBlockMetadata(x, y, z);
			world.setBlockMetadataWithNotify(x, y, z, meta | 8, 2);
		}
	}
	
	@Override
	public void onUnshield(World world, int x, int y, int z){
		int meta = world.getBlockMetadata(x, y, z);
		world.setBlockMetadataWithNotify(x, y, z, meta & 7, 2);
	}

	@Override
	public TileShield createNewTileEntity(World world) {
		return new TileShield();
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World w, int x, int y, int z){
		return AxisAlignedBB.getAABBPool().getAABB(x, y, z, x+1, y+1, z+1);
	}
	
	@Override
	public void addCollisionBoxesToList(World w, int x, int y, int z, AxisAlignedBB aabb, List l, Entity e){
		TileEntity te = w.getBlockTileEntity(x, y, z);
		if(te instanceof TileShield){
			TileShieldEmitter emitter = ((TileShield)te).getEmitter();
			if(emitter == null || !emitter.isShieldOn()) return;
			ShieldPermissions permissions = emitter.getPermissions();
			if(!permissions.isEntityAllowed(e, true)){
				this.setBlockBounds(0, 0, 0, 1, 1, 1);
				super.addCollisionBoxesToList(w, x, y, z, aabb, l, e);
				this.setBlockBoundsBasedOnState(w, x, y, z);
			}
		}
	}
}