package stargatetech2.common.base;

import stargatetech2.StargateTech2;
import stargatetech2.common.util.IconRegistry;
import stargatetech2.common.util.MaterialNaquadah;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;

public class BaseBlock extends Block{
	protected String textureName;
	
	public BaseBlock(String name){
		this(name, false, true);
	}
	
	public BaseBlock(String name, boolean breakable, boolean requiresTool) {
		super(StargateTech2.instance.config.getBlockID(name), requiresTool ? MaterialNaquadah.unbreakable : MaterialNaquadah.breakable);
		this.setUnlocalizedName(name);
		textureName = name;
		if(!breakable){
			this.setBlockUnbreakable();
			this.setResistance(20000000F);
		}
	}
	
	@Override
	public void registerIcons(IconRegister register){
		IconRegistry.loadBlocks(register);
		blockIcon = IconRegistry.getBlockIcon(textureName);
	}
	
	@Override
	public int getMobilityFlag(){
		return 2;
	}
}