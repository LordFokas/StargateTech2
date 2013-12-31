package stargatetech2.core.rendering;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;
import stargatetech2.common.base.BaseISBRH;
import stargatetech2.core.block.BlockBusCable;

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
		if(cable.canConnectBus(world, x, y, z, ForgeDirection.DOWN, 1)){
			renderer.setRenderBounds(F05, 0, F05, F11, F05, F11);
			renderer.renderStandardBlock(cable, x, y, z);
		}
		if(cable.canConnectBus(world, x, y, z, ForgeDirection.UP, 0)){
			renderer.setRenderBounds(F05, F11, F05, F11, 1, F11);
			renderer.renderStandardBlock(cable, x, y, z);
		}
		if(cable.canConnectBus(world, x, y, z, ForgeDirection.NORTH, 3)){
			renderer.setRenderBounds(F05, F05, 0, F11, F11, F05);
			renderer.renderStandardBlock(cable, x, y, z);
		}
		if(cable.canConnectBus(world, x, y, z, ForgeDirection.SOUTH, 2)){
			renderer.setRenderBounds(F05, F05, F11, F11, F11, 1);
			renderer.renderStandardBlock(cable, x, y, z);
		}
		if(cable.canConnectBus(world, x, y, z, ForgeDirection.WEST, 5)){
			renderer.setRenderBounds(0, F05, F05, F05, F11, F11);
			renderer.renderStandardBlock(cable, x, y, z);
		}
		if(cable.canConnectBus(world, x, y, z, ForgeDirection.EAST, 4)){
			renderer.setRenderBounds(F11, F05, F05, 1, F11, F11);
			renderer.renderStandardBlock(cable, x, y, z);
		}
		renderer.renderAllFaces = allFaces;
		return true;
	}
}
