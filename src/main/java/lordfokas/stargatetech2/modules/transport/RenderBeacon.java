package lordfokas.stargatetech2.modules.transport;

import lordfokas.stargatetech2.ZZ_THRASH.BaseISBRH_THRASH;
import lordfokas.stargatetech2.modules.ModuleTransport;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class RenderBeacon extends BaseISBRH_THRASH{
	private static final RenderBeacon INSTANCE = new RenderBeacon();
	
	public static RenderBeacon instance(){
		return INSTANCE;
	}
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		BlockBeacon beacon = (BlockBeacon) block;
		int meta = world.getBlockMetadata(x, y, z);
		boolean handled = true;
		beacon.setBlockBounds(0.25f, 0.25f, 0.25f, 0.75f, 0.75f, 0.75f);
		if(meta == BlockBeacon.META_TRANSCEIVER){
			renderWorldTransceiver(beacon, renderer, x, y, z);
		}else if(meta == BlockBeacon.META_ANTENNA){
			renderWorldAntenna(beacon, renderer, x, y, z);
		}else if(meta == BlockBeacon.META_CONSOLE){
			renderWorldConsole(beacon, renderer, x, y, z);
		}else if(meta == BlockBeacon.META_MATTERGRID){
			renderWorldMatterGrid(beacon, renderer, x, y, z, world);
		}else handled = false;
		beacon.setBlockBounds(0, 0, 0, 1, 1, 1);
		return handled;
	}
	
	@Override
	public void renderInventoryBlock(Block block, int meta, int modelID, RenderBlocks renderer){
		BlockBeacon beacon = (BlockBeacon) block;
		if(meta == BlockBeacon.META_TRANSCEIVER){
			renderItemTransceiver(renderer, beacon);
		}else if(meta == BlockBeacon.META_ANTENNA){
			renderItemAntenna(renderer, beacon);
		}else super.renderInventoryBlock(block, meta, modelID, renderer);
	}
	
	private void renderWorldTransceiver(BlockBeacon beacon, RenderBlocks renderer, int x, int y, int z){
		IIcon base = beacon.getBaseIcon(0, 0);
		IIcon top = beacon.getBaseIcon(1, 0);
		IIcon side = beacon.getBaseIcon(2, 0);
		IIcon[] map = new IIcon[]{base, base, side, side, side, side, side};
		
		beacon.setOverride(map);
		renderWorldCuboidWithBounds(beacon, renderer, x, y, z, 0, 0, 0, 1, F04, 1);
		map[1] = top;
		renderWorldCuboidWithBounds(beacon, renderer, x, y, z, 0, F08, 0, 1, F12, 1);
		renderWorldCuboidWithBounds(beacon, renderer, x, y, z, F01, F04, F01, F15, F08, F15);
		renderWorldCuboidWithBounds(beacon, renderer, x, y, z, F02, F12, F02, F14, 1, F14);
		beacon.restoreTextures();
	}
	
	private void renderItemTransceiver(RenderBlocks renderer, BlockBeacon beacon){
		IIcon base = beacon.getBaseIcon(0, 0);
		IIcon top = beacon.getBaseIcon(1, 0);
		IIcon side = beacon.getBaseIcon(2, 0);
		IIcon[] map = new IIcon[]{base, base, side, side, side, side, side};
		
		beacon.setOverride(map);
		renderer.setRenderBounds(0, 0, 0, 1, F04, 1);
		renderInventoryCuboid(beacon, BlockBeacon.META_TRANSCEIVER, renderer, false);
		map[1] = top;
		renderer.setRenderBounds(0, F08, 0, 1, F12, 1);
		renderInventoryCuboid(beacon, BlockBeacon.META_TRANSCEIVER, renderer, false);
		renderer.setRenderBounds(F01, F04, F01, F15, F08, F15);
		renderInventoryCuboid(beacon, BlockBeacon.META_TRANSCEIVER, renderer, false);
		renderer.setRenderBounds(F02, F12, F02, F14, 1, F14);
		renderInventoryCuboid(beacon, BlockBeacon.META_TRANSCEIVER, renderer, false);
		beacon.restoreTextures();
	}
	
	private void renderWorldAntenna(BlockBeacon beacon, RenderBlocks renderer, int x, int y, int z){
		renderWorldCuboidWithBounds(beacon, renderer, x, y, z, F03, 0, F03, F06, 1, F06);
		renderWorldCuboidWithBounds(beacon, renderer, x, y, z, F10, 0, F03, F13, 1, F06);
		renderWorldCuboidWithBounds(beacon, renderer, x, y, z, F03, 0, F10, F06, 1, F13);
		renderWorldCuboidWithBounds(beacon, renderer, x, y, z, F10, 0, F10, F13, 1, F13);
	}
	
	private void renderItemAntenna(RenderBlocks renderer, BlockBeacon beacon){
		renderer.setRenderBounds(F03, 0, F03, F06, 1, F06);
		renderInventoryCuboid(beacon, BlockBeacon.META_ANTENNA, renderer, false);
		renderer.setRenderBounds(F10, 0, F03, F13, 1, F06);
		renderInventoryCuboid(beacon, BlockBeacon.META_ANTENNA, renderer, false);
		renderer.setRenderBounds(F03, 0, F10, F06, 1, F13);
		renderInventoryCuboid(beacon, BlockBeacon.META_ANTENNA, renderer, false);
		renderer.setRenderBounds(F10, 0, F10, F13, 1, F13);
		renderInventoryCuboid(beacon, BlockBeacon.META_ANTENNA, renderer, false);
	}
	
	private void renderWorldConsole(BlockBeacon beacon, RenderBlocks renderer, int x, int y, int z){
		renderWorldCuboidWithBounds(beacon, renderer, x, y, z, 0, 0, 0, 1, F02, 1);
		renderWorldCuboidWithBounds(beacon, renderer, x, y, z, 0, F14, 0, 1, 1, 1);
		renderWorldCuboidWithBounds(beacon, renderer, x, y, z, 0, F02, 0, F02, F14, F02);
		renderWorldCuboidWithBounds(beacon, renderer, x, y, z, F14, F02, 0, 1, F14, F02);
		renderWorldCuboidWithBounds(beacon, renderer, x, y, z, 0, F02, F14, F02, F14, 1);
		renderWorldCuboidWithBounds(beacon, renderer, x, y, z, F14, F02, F14, 1, F14, 1);
		renderWorldCuboidWithBounds(beacon, renderer, x, y, z, F01, F01, F01, F15, F15, F15);
	}
	
	private void renderWorldMatterGrid(BlockBeacon beacon, RenderBlocks renderer, int x, int y, int z, IBlockAccess world){
		boolean top, bottom, left, right, topleft, topright, bottomleft, bottomright;
		top = isMatterGrid(world, x, y, z+1);
		bottom = isMatterGrid(world, x, y, z-1);
		right = isMatterGrid(world, x+1, y, z);
		left = isMatterGrid(world, x-1, y, z);
		topright = isMatterGrid(world, x+1, y, z+1);
		topleft = isMatterGrid(world, x-1, y, z+1);
		bottomright = isMatterGrid(world, x+1, y, z-1);
		bottomleft = isMatterGrid(world, x-1, y, z-1);
		
		renderWorldCuboidWithBounds(beacon, renderer, x, y, z, 0, 0, 0, 1, F15, 1);
		
		if(!top) renderWorldCuboidWithBounds(beacon, renderer, x, y, z, F01, F15, F15, F15, 1, 1);
		if(!bottom) renderWorldCuboidWithBounds(beacon, renderer, x, y, z, F01, F15, 0, F15, 1, F01);
		if(!right) renderWorldCuboidWithBounds(beacon, renderer, x, y, z, F15, F15, F01, 1, 1, F15);
		if(!left) renderWorldCuboidWithBounds(beacon, renderer, x, y, z, 0, F15, F01, F01, 1, F15);
		if(!(top && right && topright)) renderWorldCuboidWithBounds(beacon, renderer, x, y, z, F15, F15, F15, 1, 1, 1);
		if(!(top && left && topleft)) renderWorldCuboidWithBounds(beacon, renderer, x, y, z, 0, F15, F15, F01, 1, 1);
		if(!(bottom && right && bottomright)) renderWorldCuboidWithBounds(beacon, renderer, x, y, z, F15, F15, 0, 1, 1, F01);
		if(!(bottom && left && bottomleft)) renderWorldCuboidWithBounds(beacon, renderer, x, y, z, 0, F15, 0, F01, 1, F01);
	}
	
	private boolean isMatterGrid(IBlockAccess world, int x, int y, int z){
		boolean beacon = world.getBlock(x, y, z) == ModuleTransport.beacon;
		boolean meta = world.getBlockMetadata(x, y, z) == BlockBeacon.META_MATTERGRID;
		return beacon && meta;
	}
}