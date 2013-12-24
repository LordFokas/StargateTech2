package stargatetech2.common.machine;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import stargatetech2.api.ITabletAccess;
import stargatetech2.common.base.BaseBlockContainer;
import stargatetech2.common.base.BaseTileEntity;
import stargatetech2.common.packet.PacketOpenGUI;
import stargatetech2.common.reference.TextureReference;
import stargatetech2.common.util.GUIHandler.Screen;
import stargatetech2.common.util.Helper;
import stargatetech2.common.util.IconRegistry;
import stargatetech2.core.tileentity.TileShieldEmitter;
import buildcraft.api.tools.IToolWrench;

public abstract class BlockMachine extends BaseBlockContainer implements ITabletAccess{
	private Screen screen;
	
	public BlockMachine(String uName, Screen scr){
		super(uName);
		screen = scr;
	}
	
	@Override
	public void onBlockPlacedBy(World w, int x, int y, int z, EntityLivingBase living, ItemStack stack){
		ForgeDirection dir = Helper.yaw2dir(living.rotationYaw);
		w.setBlockMetadataWithNotify(x, y, z, dir.ordinal(), 2);
		TileEntity te = w.getBlockTileEntity(x, y, z);
		if(te instanceof TileShieldEmitter && living instanceof EntityPlayer){
			((TileShieldEmitter)te).setOwner(((EntityPlayer)living).getDisplayName());
		}
	}
	
	@Override
	public Icon getBaseIcon(int side, int meta){
		switch(side){
			case 0: return IconRegistry.blockIcons.get(TextureReference.MACHINE_BOTTOM);
			case 1: return IconRegistry.blockIcons.get(TextureReference.MACHINE_TOP);
			case 3: return blockIcon;
			default: return IconRegistry.blockIcons.get(TextureReference.MACHINE_SIDE);
		}
	}

	@Override
	public boolean onTabletAccess(EntityPlayer player, World world, int x, int y, int z) {
		if(screen != null && canAccess(player, world, x, y, z)){
			PacketOpenGUI packet = new PacketOpenGUI();
			packet.guiID = screen.ordinal();
			packet.x = x;
			packet.y = y;
			packet.z = z;
			packet.sendToServer();
			TileEntity te = world.getBlockTileEntity(x, y, z);
			if(te instanceof BaseTileEntity){
				((BaseTileEntity)te).updateClients();
			}
			return true;
		}
		return false;
	}
	
	protected boolean canAccess(EntityPlayer player, World world, int x, int y, int z){
		return true;
	}
	
	@Override
	public int getRenderType(){
		return RenderBlockMachine.instance().renderID;
	}
	
	@Override
	public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer p, int s, float hx, float hy, float hz){
		ItemStack stack = p.inventory.getCurrentItem();
		Item item = stack != null ? stack.getItem() : null;
		if(item instanceof IToolWrench && canAccess(p, w, x, y, z)){
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
	
	public abstract String getFace(IBlockAccess world, int x, int y, int z);
	public abstract String getGlow(IBlockAccess world, int x, int y, int z);
}