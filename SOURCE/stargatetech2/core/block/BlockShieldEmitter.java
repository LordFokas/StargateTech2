package stargatetech2.core.block;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import stargatetech2.StargateTech2;
import stargatetech2.api.ITabletAccess;
import stargatetech2.common.base.BaseBlockContainer;
import stargatetech2.common.reference.BlockReference;
import stargatetech2.common.reference.TextureReference;
import stargatetech2.common.registry.IconRegistry;
import stargatetech2.common.util.GUIHandler;
import stargatetech2.common.util.Helper;
import stargatetech2.core.rendering.RenderShieldEmitter;
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
		player.openGui(StargateTech2.instance, GUIHandler.Screen.SHIELD_EMITTER.ordinal(), world, x, y, z);
		return true;
	}
	
	@Override
	public void onBlockPlacedBy(World w, int x, int y, int z, EntityLivingBase living, ItemStack stack){
		ForgeDirection dir = Helper.yaw2dir(living.rotationYaw);
		TileEntity te = w.getBlockTileEntity(x, y, z);
		if(te instanceof TileShieldEmitter){
			w.setBlockMetadataWithNotify(x, y, z, dir.ordinal(), 2);
		}
	}
	
	@Override
	public int getRenderType(){
		return RenderShieldEmitter.instance().getRenderId();
	}
	
	@Override
	public Icon getBaseIcon(int side, int meta){
		switch(side){
			case 0: return IconRegistry.icons.get(TextureReference.MACHINE_BOTTOM);
			case 1: return IconRegistry.icons.get(TextureReference.MACHINE_TOP);
			case 3: return blockIcon;
			default: return IconRegistry.icons.get(TextureReference.MACHINE_SIDE);
		}
	}
}