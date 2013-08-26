package stargatetech2.core.block;

import net.minecraftforge.common.MinecraftForge;
import stargatetech2.common.base.BaseBlock;
import stargatetech2.common.reference.BlockReference;
import stargatetech2.core.rendering.RenderNaquadahOre;

public class BlockNaquadahOre extends BaseBlock {
	
	public BlockNaquadahOre(){
		super(BlockReference.NAQUADAH_ORE, true, true);
		this.setHardness(3.0F);
		this.setResistance(5.0F);
		MinecraftForge.setBlockHarvestLevel(this, "pickaxe", 2);
	}
	
	public int getRenderType(){
		return RenderNaquadahOre.instance().getRenderId();
	}
}