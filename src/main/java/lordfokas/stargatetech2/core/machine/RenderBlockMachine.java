package lordfokas.stargatetech2.core.machine;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import lordfokas.stargatetech2.core.IconRegistry;
import lordfokas.stargatetech2.core.base.BaseISBRH;
import lordfokas.stargatetech2.core.reference.TextureReference;

public class RenderBlockMachine extends BaseISBRH {
	private static final RenderBlockMachine INSTANCE = new RenderBlockMachine();
	
	public static RenderBlockMachine instance(){
		return INSTANCE;
	}
	
	@Override
	public boolean renderWorldBlock(IBlockAccess w, int x, int y, int z, Block block, int modelId, RenderBlocks renderer){
		BlockMachine machine = (BlockMachine) block;
		int metadata = w.getBlockMetadata(x, y, z);
		IIcon mside = IconRegistry.blockIcons.get(TextureReference.MACHINE_SIDE);
		IIcon msidei = IconRegistry.blockIcons.get(TextureReference.MACHINE_SIDE_I);
		IIcon mtop = IconRegistry.blockIcons.get(TextureReference.MACHINE_TOP);
		IIcon mtopi = IconRegistry.blockIcons.get(TextureReference.MACHINE_TOP_I);
		IIcon mbot = IconRegistry.blockIcons.get(TextureReference.MACHINE_BOTTOM);
		IIcon mboti = IconRegistry.blockIcons.get(TextureReference.MACHINE_BOTTOM_I);
		IIcon[] map = new IIcon[6];
		FaceColor[] colors = machine.getTextureMap(w, x, y, z);
		colors[metadata] = FaceColor.VOID;
		for(int face = 0; face < 6; face++){
			switch(face){
				case 0:
					map[face] = colors[face].isColored() ? mboti : mbot;
					break;
				case 1:
					map[face] = colors[face].isColored() ? mtopi : mtop;
					break;
				default:
					map[face] = colors[face].isColored() ? msidei : mside;
					break;
			}
		}
		map[metadata] = machine.getFaceForMeta(metadata);
		machine.setOverride(map);
		renderer.renderStandardBlock(machine, x, y, z);
		for(int i = 0; i < 6; i++){
			map[i] = IconRegistry.blockIcons.get(colors[i].getTexture());
		}
		renderer.renderStandardBlock(machine, x, y, z);
		machine.restoreTextures();
		return true;
	}
}