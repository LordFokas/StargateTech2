package stargatetech2.core.block;

import buildcraft.api.tools.IToolWrench;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import stargatetech2.common.base.BaseBlockContainer;
import stargatetech2.common.reference.BlockReference;
import stargatetech2.common.reference.TextureReference;
import stargatetech2.common.util.Helper;
import stargatetech2.common.util.IconRegistry;
import stargatetech2.core.tileentity.TileTransportRing;

public class BlockTransportRing extends BaseBlockContainer{

	public BlockTransportRing() {
		super(BlockReference.TRANSPORT_RING);
	}
	
	@Override
	public Icon getBaseIcon(int side, int meta){
		switch(side){
			case 0: return IconRegistry.blockIcons.get(TextureReference.MACHINE_BOTTOM);
			case 1: return blockIcon;
			default: return IconRegistry.blockIcons.get(TextureReference.MACHINE_SIDE);
		}
	}
	
	@Override
	public boolean isOpaqueCube(){
		return false;
	}
	
	@Override
	public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer p, int s, float hx, float hy, float hz){
		ItemStack stack = p.inventory.getCurrentItem();
		Item item = stack != null ? stack.getItem() : null;
		if(item instanceof IToolWrench){
			IToolWrench wrench = (IToolWrench) item;
			if(wrench.canWrench(p, x, y, z)){
				dropBlockAsItem(w, x, y, z, 0, 0);
				w.setBlock(x, y, z, 0, 0, 3);
				wrench.wrenchUsed(p, x, y, z);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void onBlockPlacedBy(World w, int x, int y, int z, EntityLivingBase living, ItemStack stack){
		if(w.isRemote) return;
		TileTransportRing ring = (TileTransportRing) w.getBlockTileEntity(x, y, z);
		ring.link();
	}
	
	@Override
	protected TileTransportRing createTileEntity(int metadata) {
		return new TileTransportRing();
	}
}