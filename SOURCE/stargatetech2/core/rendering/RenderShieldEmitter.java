package stargatetech2.core.rendering;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;
import stargatetech2.common.base.BaseISBRH;
import stargatetech2.common.reference.TextureReference;
import stargatetech2.common.registry.IconRegistry;
import stargatetech2.core.block.BlockShieldEmitter;
import stargatetech2.core.tileentity.TileShieldEmitter;

public class RenderShieldEmitter extends BaseISBRH {
	private static final RenderShieldEmitter INSTANCE = new RenderShieldEmitter();
	
	public static RenderShieldEmitter instance(){
		return INSTANCE;
	}
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		BlockShieldEmitter bse = (BlockShieldEmitter) block;
		TileEntity te = world.getBlockTileEntity(x, y, z);
		int direction = -1;
		if(te instanceof TileShieldEmitter){
			int d = world.getBlockMetadata(x, y, z);
			if(d > 1 && d < 6) direction = d;
		}
		Icon mside = IconRegistry.icons.get(TextureReference.MACHINE_SIDE);
		Icon mtop = IconRegistry.icons.get(TextureReference.MACHINE_TOP);
		Icon mbot = IconRegistry.icons.get(TextureReference.MACHINE_BOTTOM);
		Icon front = IconRegistry.icons.get(TextureReference.FACE_SHIELD_EMITTER);
		Icon glow = IconRegistry.icons.get(TextureReference.GLOW_SHIELD_EMITTER);
		Icon[] tmap = new Icon[]{mbot, mtop, mside, mside, mside, mside, mside};
		if(direction != -1){
			tmap[direction] = front;
			Tessellator tessellator = Tessellator.instance;
			tessellator.setColorOpaque_I(0xFFFFFF);
			tessellator.setBrightness(220);
			switch(ForgeDirection.getOrientation(direction)){
				case SOUTH:
					renderer.renderFaceZPos(bse, x, y, z, glow);
					break;
				case EAST:
					renderer.renderFaceXPos(bse, x, y, z, glow);
					break;
				case NORTH:
					renderer.renderFaceZNeg(bse, x, y, z, glow);
					break;
				case WEST:
					renderer.renderFaceXNeg(bse, x, y, z, glow);
					break;
				default: break; 
			}
		}
		bse.setOverride(tmap);
		renderer.renderStandardBlock(bse, x, y, z);
		bse.restoreTextures();
		return true;
	}

}
