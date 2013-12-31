package stargatetech2.core.rendering;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;
import stargatetech2.common.base.BaseISBRH;
import stargatetech2.common.reference.TextureReference;
import stargatetech2.common.util.IconRegistry;
import stargatetech2.core.block.BlockBusCable;
import stargatetech2.core.network.bus.Connection;

public class RenderBusCable extends BaseISBRH {
	private static final RenderBusCable INSTANCE = new RenderBusCable();
	
	public static RenderBusCable instance(){
		return INSTANCE;
	}
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		BlockBusCable cable = (BlockBusCable) block;
		boolean allFaces = renderer.renderAllFaces;
		renderer.renderAllFaces = true;
		renderer.setRenderBounds(F05, F05, F05, F11, F11, F11);
		renderer.renderStandardBlock(cable, x, y, z);
		Connection x0, x1, y0, y1, z0, z1;
		Icon plug = IconRegistry.blockIcons.get(TextureReference.BUS_CABLE_PLUG);
		y0 = cable.getBusConnection(world, x, y, z, ForgeDirection.DOWN);
		if(y0.isConnected()){
			renderer.setRenderBounds(F05, 0, F05, F11, F05, F11);
			renderer.renderStandardBlock(cable, x, y, z);
		}
		y1 = cable.getBusConnection(world, x, y, z, ForgeDirection.UP);
		if(y1.isConnected()){
			renderer.setRenderBounds(F05, F11, F05, F11, 1, F11);
			renderer.renderStandardBlock(cable, x, y, z);
		}
		z0 = cable.getBusConnection(world, x, y, z, ForgeDirection.NORTH);
		if(z0.isConnected()){
			renderer.setRenderBounds(F05, F05, 0, F11, F11, F05);
			renderer.renderStandardBlock(cable, x, y, z);
		}
		z1 = cable.getBusConnection(world, x, y, z, ForgeDirection.SOUTH);
		if(z1.isConnected()){
			renderer.setRenderBounds(F05, F05, F11, F11, F11, 1);
			renderer.renderStandardBlock(cable, x, y, z);
		}
		x0 = cable.getBusConnection(world, x, y, z, ForgeDirection.WEST);
		if(x0.isConnected()){
			renderer.setRenderBounds(0, F05, F05, F05, F11, F11);
			renderer.renderStandardBlock(cable, x, y, z);
		}
		x1 = cable.getBusConnection(world, x, y, z, ForgeDirection.EAST);
		if(x1.isConnected()){
			renderer.setRenderBounds(F11, F05, F05, 1, F11, F11);
			renderer.renderStandardBlock(cable, x, y, z);
		}
		renderer.setOverrideBlockTexture(plug);
		renderer.flipTexture = true;
		if(y0.hasPlug()){
			renderer.setRenderBounds(F04, 0, F04, F12, F02, F12);
			renderer.renderStandardBlock(cable, x, y, z);
		}
		if(y1.hasPlug()){
			renderer.setRenderBounds(F04, F14, F04, F12, 1, F12);
			renderer.renderStandardBlock(cable, x, y, z);
		}
		if(z0.hasPlug()){
			renderer.setRenderBounds(F04, F04, 0, F12, F12, F02);
			renderer.renderStandardBlock(cable, x, y, z);
		}
		if(z1.hasPlug()){
			renderer.setRenderBounds(F04, F04, F14, F12, F12, 1);
			renderer.renderStandardBlock(cable, x, y, z);
		}
		if(x0.hasPlug()){
			renderer.setRenderBounds(0, F04, F04, F02, F12, F12);
			renderer.renderStandardBlock(cable, x, y, z);
		}
		if(x1.hasPlug()){
			renderer.setRenderBounds(F14, F04, F04, 1, F12, F12);
			renderer.renderStandardBlock(cable, x, y, z);
		}
		renderer.setOverrideBlockTexture(null);
		renderer.renderAllFaces = allFaces;
		return true;
	}
}
