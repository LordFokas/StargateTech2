package stargatetech2.core.block;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import stargatetech2.api.ITabletAccess;
import stargatetech2.common.base.BaseBlockContainer;
import stargatetech2.common.base.BaseTileEntity;
import stargatetech2.common.packet.PacketOpenGUI;
import stargatetech2.common.reference.BlockReference;
import stargatetech2.common.util.GUIHandler.Screen;
import stargatetech2.core.item.ItemBlockNaquadahCapacitor;
import stargatetech2.core.item.ItemNaquadah;
import stargatetech2.core.rendering.RenderNaquadahCapacitor;
import stargatetech2.core.tileentity.TileNaquadahCapacitor;
import buildcraft.api.tools.IToolWrench;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockNaquadahCapacitor extends BaseBlockContainer implements ITabletAccess{
	
	public BlockNaquadahCapacitor() {
		super(BlockReference.NAQUADAH_CAPACITOR);
	}
	
	@Override
	protected BaseTileEntity createTileEntity(int metadata) {
		return new TileNaquadahCapacitor(metadata);
	}
	
	@Override
	public int getRenderType(){
		return RenderNaquadahCapacitor.instance().getRenderId();
	}
	
	@Override
	public boolean onTabletAccess(EntityPlayer player, World world, int x, int y, int z) {
		PacketOpenGUI packet = new PacketOpenGUI();
		packet.guiID = Screen.NAQUADAH_CAPACITOR.ordinal();
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
	
	@Override
	public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer p, int s, float hx, float hy, float hz){
		ItemStack stack = p.inventory.getCurrentItem();
		Item item = stack != null ? stack.getItem() : null;
		if(item instanceof IToolWrench){
			IToolWrench wrench = (IToolWrench) item;
			if(wrench.canWrench(p, x, y, z)){
				dropBlockWithTileEntity(w, x, y, z);
				w.setBlock(x, y, z, 0, 0, 3);
				wrench.wrenchUsed(p, x, y, z);
				return true;
			}
		}else if(item instanceof ItemNaquadah){
			TileEntity te = w.getBlockTileEntity(x, y, z);
			if(te instanceof TileNaquadahCapacitor){
				TileNaquadahCapacitor capacitor = (TileNaquadahCapacitor) te;
				int tier = ItemNaquadah.getTierForMeta(stack.getItemDamage());
				if(tier != 0){
					if(capacitor.canUpgrade(tier)){
						int newTier = capacitor.upgrade(tier);
						stack.setItemDamage(ItemNaquadah.getMetaForTier(newTier));
					}
				}
			}
		}
		return false;
	}
	
	private void dropBlockWithTileEntity(World w, int x, int y, int z){
		if(w.isRemote) return;
		TileEntity te = w.getBlockTileEntity(x, y, z);
		if(te instanceof TileNaquadahCapacitor){
			NBTTagCompound nbt = new NBTTagCompound();
			te.writeToNBT(nbt);
			ItemStack stack = new ItemStack(this);
			stack.setTagCompound(nbt);
			w.spawnEntityInWorld(new EntityItem(w, ((double)x) + 0.5, ((double)y) + 0.5, ((double)z) + 0.5, stack));
		}else{
			dropBlockAsItem(w, x, y, z, 0, 0);
		}
	}
	
	@Override
	public void registerBlock(){
		GameRegistry.registerBlock(this, ItemBlockNaquadahCapacitor.class, getUnlocalizedName());
	}
}