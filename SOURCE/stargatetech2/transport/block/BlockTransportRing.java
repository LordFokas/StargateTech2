package stargatetech2.transport.block;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import stargatetech2.core.base.BaseBlockContainer;
import stargatetech2.core.reference.BlockReference;
import stargatetech2.core.reference.TextureReference;
import stargatetech2.core.util.IconRegistry;
import stargatetech2.transport.tileentity.TileTransportRing;
import buildcraft.api.tools.IToolWrench;

public class BlockTransportRing extends BaseBlockContainer{

	public BlockTransportRing() {
		super(BlockReference.TRANSPORT_RING);
		setIsAbstractBusBlock();
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