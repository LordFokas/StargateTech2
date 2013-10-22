package stargatetech2.common.machine;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;
import stargatetech2.common.base.BaseISBRH;
import stargatetech2.common.reference.TextureReference;
import stargatetech2.common.util.IconRegistry;
import stargatetech2.core.block.BlockParticleIonizer;

public class RenderBlockMachine extends BaseISBRH {
	private static final RenderBlockMachine INSTANCE = new RenderBlockMachine();
	
	public static RenderBlockMachine instance(){
		return INSTANCE;
	}
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer){
		BlockMachine machine = (BlockMachine) block;
		int direction = -1;
		int d = world.getBlockMetadata(x, y, z);
		if(d > 1 && d < 6) direction = d;
		Icon mside = IconRegistry.blockIcons.get(TextureReference.MACHINE_SIDE);
		Icon mtop = IconRegistry.blockIcons.get(TextureReference.MACHINE_TOP);
		Icon mbot = IconRegistry.blockIcons.get(TextureReference.MACHINE_BOTTOM);
		Icon face = IconRegistry.blockIcons.get(machine.getFace(world, x, y, z));
		Icon glow = IconRegistry.blockIcons.get(machine.getGlow(world, x, y, z));
		Icon[] tmap = new Icon[]{mbot, mtop, mside, mside, mside, mside, mside};
		if(direction != -1){
			tmap[direction] = face;
			Tessellator tessellator = Tessellator.instance;
			tessellator.setColorOpaque_I(0xFFFFFF);
			tessellator.setBrightness(220);
			switch(ForgeDirection.getOrientation(direction)){
				case SOUTH:
					renderer.renderFaceZPos(machine, x, y, z, glow);
					break;
				case EAST:
					renderer.renderFaceXPos(machine, x, y, z, glow);
					break;
				case NORTH:
					renderer.renderFaceZNeg(machine, x, y, z, glow);
					break;
				case WEST:
					renderer.renderFaceXNeg(machine, x, y, z, glow);
					break;
				default: break; 
			}
		}
		machine.setOverride(tmap);
		renderer.renderStandardBlock(machine, x, y, z);
		machine.restoreTextures();
		return true;
	}

}
