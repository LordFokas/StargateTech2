package stargatetech2.core.block;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import stargatetech2.api.ITabletAccess;
import stargatetech2.api.stargate.ITileStargate;
import stargatetech2.common.base.BaseBlockContainer;
import stargatetech2.common.base.BaseTileEntity;
import stargatetech2.common.reference.BlockReference;
import stargatetech2.common.util.Helper;
import stargatetech2.core.rendering.RenderStargateBlock;
import stargatetech2.core.tileentity.TileShieldEmitter;
import stargatetech2.core.tileentity.TileStargate;
import stargatetech2.core.tileentity.TileStargateBase;
import stargatetech2.core.tileentity.TileStargateRing;
import stargatetech2.core.worldgen.lists.StargateBuildList;

public class BlockStargate extends BaseBlockContainer implements ITabletAccess{
	public static final int META_BASE = 0x0E;
	public static final int META_RING = 0x0F;

	public BlockStargate() {
		super(BlockReference.STARGATE);
	}
	
	@Override
	public int getRenderType(){
		return RenderStargateBlock.instance().getRenderId();
	}
	
	@Override
	public boolean isOpaqueCube(){
		return false;
	}
	
	@Override
	protected BaseTileEntity createTileEntity(int metadata){
		switch(metadata){
			case META_BASE: return new TileStargateBase();
			case META_RING: return new TileStargateRing();
			default: return new TileStargate();
		}
	}

	@Override
	public boolean onTabletAccess(EntityPlayer player, World world, int x, int y, int z){
		String message;
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if(te instanceof ITileStargate){
			message = "This Stargate uses the address " + EnumChatFormatting.GOLD.toString() + ((ITileStargate)te).getAddress();
			Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(message);
		}
		return true;
	}
	
	@Override
	public void onBlockPlacedBy(World w, int x, int y, int z, EntityLivingBase living, ItemStack stack){
		ForgeDirection dir = Helper.yaw2dir(living.rotationYaw);
		w.setBlockMetadataWithNotify(x, y, z, dir.ordinal(), 2);
		int xm = dir.offsetZ * dir.offsetZ;
		int zm = dir.offsetX * dir.offsetX;
		if(xm == 1){
			StargateBuildList.SGX.buildStargate(w, x, y, z, x, y, z);
		}else if(zm == 1){
			StargateBuildList.SGZ.buildStargate(w, x, y, z, x, y, z);
		}
	}
}