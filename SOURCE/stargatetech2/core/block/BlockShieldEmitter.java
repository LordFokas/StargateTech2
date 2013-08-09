package stargatetech2.core.block;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import stargatetech2.api.ITabletAccess;
import stargatetech2.common.base.BaseBlockContainer;
import stargatetech2.common.reference.BlockReference;
import stargatetech2.core.tileentity.TileShieldEmitter;

public class BlockShieldEmitter extends BaseBlockContainer implements ITabletAccess{

	public BlockShieldEmitter() {
		super(BlockReference.SHIELD_EMITTER);
	}

	@Override
	protected TileShieldEmitter createTileEntity(int metadata) {
		return new TileShieldEmitter();
	}

	@Override
	public boolean onTabletAccess(EntityPlayer player, World world, int x, int y, int z) {
		
		return true;
	}
}