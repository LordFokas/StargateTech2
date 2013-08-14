package stargatetech2.core.block;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import stargatetech2.common.base.BaseBlockContainer;
import stargatetech2.common.reference.BlockReference;
import stargatetech2.core.item.ItemBlockShield;
import stargatetech2.core.tileentity.TileShield;
import stargatetech2.core.tileentity.TileShieldEmitter;
import stargatetech2.core.util.ShieldPermissions;

public class BlockShield extends BaseBlockContainer { 
	public BlockShield() {
		super(BlockReference.SHIELD);
		setCreativeTab(null);
		setLightValue(1.0F);
	}
	
	@Override
	public int getRenderBlockPass(){
		return 1;
	}
	
	@Override
	public boolean isOpaqueCube(){
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock(){
		return false;
	}
	
	@Override
	protected TileShield createTileEntity(int metadata) {
		return new TileShield();
	}
	
	@Override
	public void addCollisionBoxesToList(World w, int x, int y, int z, AxisAlignedBB aabb, List l, Entity e){
		TileEntity te = w.getBlockTileEntity(x, y, z);
		if(te instanceof TileShield){
			ShieldPermissions permissions = ((TileShield)te).getPermissions();
			if(!permissions.isEntityAllowed(e)){
				super.addCollisionBoxesToList(w, x, y, z, aabb, l, e);
			}
		}
	}
	
	@Override
	public void registerBlock(){
		GameRegistry.registerBlock(this, ItemBlockShield.class, getUnlocalizedName());
	}
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z){
		return null;
	}
}
