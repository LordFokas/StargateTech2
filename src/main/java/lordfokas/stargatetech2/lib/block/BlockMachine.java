package lordfokas.stargatetech2.lib.block;

import lordfokas.stargatetech2.lib.tileentity.TileEntityMachine;
import lordfokas.stargatetech2.lib.util.TileEntityHelper;
import lordfokas.stargatetech2.util.Helper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BlockMachine extends BaseBlockContainer {
	private Class<? extends TileEntityMachine> tile;
	
	public BlockMachine(String uName, Class<? extends TileEntityMachine> tile) {
		super(uName, true, true);
		this.tile = tile;
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
		super.onBlockPlacedBy(world, x, y, z, entity, stack);
		if(entity instanceof EntityPlayerMP){
			TileEntityMachine machine = TileEntityHelper.getTileEntityAs(world, x, y, z, TileEntityMachine.class);
			// TODO: redirect the direction decision to the machine TE, it can then call the helper if it needs.
			machine.setFacing(Helper.yaw2dir(entity.rotationYaw, 0, false)); // only the yaw really matters here
		}
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