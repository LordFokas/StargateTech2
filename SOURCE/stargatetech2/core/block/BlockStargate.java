package stargatetech2.core.block;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import stargatetech2.api.ITabletAccess;
import stargatetech2.api.stargate.ITileStargate;
import stargatetech2.common.base.BaseBlockContainer;
import stargatetech2.common.base.BaseTileEntity;
import stargatetech2.common.reference.BlockReference;
import stargatetech2.core.rendering.RenderStargateBlock;
import stargatetech2.core.tileentity.TileStargate;

public class BlockStargate extends BaseBlockContainer implements ITabletAccess{

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
	protected TileStargate createTileEntity(int metadata){
		// TODO: A lot of stuff, actually.
		// Most importantly, make metadata blocks for several ring parts.
		return new TileStargate();
	}

	@Override
	public boolean onTabletAccess(EntityPlayer player, World world, int x, int y, int z){
		String message;
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if(te instanceof ITileStargate){
			message = "This Stargate uses the address " + EnumChatFormatting.GOLD.toString() + ((ITileStargate)te).getAddress().toString();
			Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(message);
		}
		return true;
	}
}
