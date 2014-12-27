package lordfokas.stargatetech2.transport;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import lordfokas.stargatetech2.core.base.BaseISBRH;

public class RenderBeacon extends BaseISBRH{
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
			renderWorldBase(beacon, renderer, x, y, z);
		}else if(meta == BlockBeacon.META_ANTENNA){
			renderWorldPole(beacon, renderer, x, y, z);
		}else handled = false;
		beacon.setBlockBounds(0, 0, 0, 1, 1, 1);
		return handled;
	}
	
	@Override
	public void renderInventoryBlock(Block block, int meta, int modelID, RenderBlocks renderer){
		BlockBeacon beacon = (BlockBeacon) block;
		if(meta == BlockBeacon.META_TRANSCEIVER){
			renderItemBase(renderer, beacon);
		}else if(meta == BlockBeacon.META_ANTENNA){
			renderItemPole(renderer, beacon);
		};
	}
	
	private void renderWorldBase(BlockBeacon beacon, RenderBlocks renderer, int x, int y, int z){
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
	
	private void renderItemBase(RenderBlocks renderer, BlockBeacon beacon){
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
	
	private void renderWorldPole(BlockBeacon beacon, RenderBlocks renderer, int x, int y, int z){
		renderWorldCuboidWithBounds(beacon, renderer, x, y, z, F03, 0, F03, F06, 1, F06);
		renderWorldCuboidWithBounds(beacon, renderer, x, y, z, F10, 0, F03, F13, 1, F06);
		renderWorldCuboidWithBounds(beacon, renderer, x, y, z, F03, 0, F10, F06, 1, F13);
		renderWorldCuboidWithBounds(beacon, renderer, x, y, z, F10, 0, F10, F13, 1, F13);
	}
	
	private void renderItemPole(RenderBlocks renderer, BlockBeacon beacon){
		renderer.setRenderBounds(F03, 0, F03, F06, 1, F06);
		renderInventoryCuboid(beacon, BlockBeacon.META_ANTENNA, renderer, false);
		renderer.setRenderBounds(F10, 0, F03, F13, 1, F06);
		renderInventoryCuboid(beacon, BlockBeacon.META_ANTENNA, renderer, false);
		renderer.setRenderBounds(F03, 0, F10, F06, 1, F13);
		renderInventoryCuboid(beacon, BlockBeacon.META_ANTENNA, renderer, false);
		renderer.setRenderBounds(F10, 0, F10, F13, 1, F13);
		renderInventoryCuboid(beacon, BlockBeacon.META_ANTENNA, renderer, false);
	}
}