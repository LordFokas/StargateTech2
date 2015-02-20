package lordfokas.stargatetech2.modules.enemy;

import java.util.List;

import lordfokas.stargatetech2.ZZ_THRASH.BaseBlockContainer__OLD_AND_FLAWED;
import lordfokas.stargatetech2.api.ITabletAccess;
import lordfokas.stargatetech2.api.shields.ShieldPermissions;
import lordfokas.stargatetech2.reference.BlockReference;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockShield extends BaseBlockContainer__OLD_AND_FLAWED implements ITabletAccess { 
	public BlockShield() {
		super(BlockReference.SHIELD);
		setCreativeTab(null);
		setLightLevel(1.0F);
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
		TileEntity te = w.getTileEntity(x, y, z);
		if(te instanceof TileShield){
			ShieldPermissions permissions = ((TileShield)te).getPermissions();
			if(!permissions.isEntityAllowed(e, true, ((TileShield)te).getOwner())){
				super.addCollisionBoxesToList(w, x, y, z, aabb, l, e);
			}
		}
	}
	
    @Override
    public boolean shouldSideBeRendered(IBlockAccess access, int x, int y, int z, int s) {
        Block b = access.getBlock(x,y,z);
        if(b instanceof BlockShield){
            return false;
        }
        
        TileEntity te = access.getTileEntity(x, y, z);
        if(te instanceof TileShield && ((TileShield)te).getController()!=null){
            return !((TileShield)te).getController().getClientContext().isShieldOn();
        }
        return true;
    }
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z){
		return null;
	}

	@Override
	public boolean onTabletAccess(EntityPlayer player, World world, int x, int y, int z) {
		TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof TileShield){
			ShieldPermissions permissions = ((TileShield)te).getPermissions();
			String message;
			if(permissions.isEntityAllowed(player, false, ((TileShield)te).getOwner())){
				message = "\u00A79 A field of Ionized Particles. It seems to invite you through.";
			}else{
				message = "\u00A79 A field of Ionized Particles. It seems to refuse your passage.";
			}
			Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(message));
		}
		return true;
	}
}
