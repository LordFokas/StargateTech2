package lordfokas.stargatetech2.integration.plugins.cc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import lordfokas.stargatetech2.core.base.BaseBlockContainer;
import lordfokas.stargatetech2.core.base.BaseTileEntity;
import lordfokas.stargatetech2.core.reference.BlockReference;
import buildcraft.api.tools.IToolWrench;

public class BlockBusAdapter extends BaseBlockContainer {
	
	public BlockBusAdapter() {
		super(BlockReference.BUS_ADAPTER);
		setIsAbstractBusBlock();
	}

	@Override
	protected BaseTileEntity createTileEntity(int metadata) {
		return new TileBusAdapter();
	}
	
	@Override
	public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer p, int s, float hx, float hy, float hz){
		ItemStack stack = p.inventory.getCurrentItem();
		Item item = stack != null ? stack.getItem() : null;
		if(item instanceof IToolWrench){
			IToolWrench wrench = (IToolWrench) item;
			if(wrench.canWrench(p, x, y, z)){
				dropBlockAsItem(w, x, y, z, 0, 0);
				w.setBlock(x, y, z, Blocks.air, 0, 3);
				wrench.wrenchUsed(p, x, y, z);
				return true;
			}
		}
		return false;
	}
}