package stargatetech2.core.block;

import net.minecraft.block.material.Material;
import net.minecraftforge.common.MinecraftForge;
import stargatetech2.core.base.BaseBlock;
import stargatetech2.core.reference.BlockReference;
import stargatetech2.core.rendering.RenderNaquadahOre;

public class BlockNaquadahOre extends BaseBlock {
	
	public BlockNaquadahOre(){
		super(BlockReference.NAQUADAH_ORE, true, Material.rock);
		this.setHardness(3.0F);
		this.setResistance(5.0F);
		MinecraftForge.setBlockHarvestLevel(this, "pickaxe", 2);
	}
	
	public int getRenderType(){
		return RenderNaquadahOre.instance().getRenderId();
	}
}