package stargatetech2.common.base;

import net.minecraft.block.Block;
import net.minecraft.util.Icon;
import stargatetech2.StargateTech2;
import stargatetech2.common.reference.ModReference;
import stargatetech2.common.util.MaterialNaquadah;
import stargatetech2.common.util.StargateTab;
import cpw.mods.fml.common.registry.GameRegistry;

public class BaseBlock extends Block{
	protected Icon[] iconOverride;
	protected boolean isOverride = false;
	
	public BaseBlock(String uName){
		this(uName, false, true);
	}
	
	public BaseBlock(String uName, boolean breakable, boolean requiresTool) {
		super(StargateTech2.instance.config.getBlockID(uName), requiresTool ? MaterialNaquadah.unbreakable : MaterialNaquadah.breakable);
		this.setUnlocalizedName(uName);
		this.func_111022_d(ModReference.MOD_ID + ":" + uName);
		if(!breakable){
			this.setBlockUnbreakable();
			this.setResistance(20000000F);
		}
		this.setCreativeTab(StargateTab.instance);
	}
	
	@Override
	public int getMobilityFlag(){
		return 2;
	}
	
	public void registerBlock(){
		GameRegistry.registerBlock(this, getUnlocalizedName());
	}
	
	public void setOverride(Icon[] icons){
		iconOverride = icons;
		isOverride = true;
	}
	
	public void restoreTextures(){
		isOverride = false;
	}
	
	@Override
	public final Icon getIcon(int side, int meta){
		if(isOverride){
			return iconOverride[side];
		}else{
			return getBaseIcon(side, meta);
		}
	}
	
	public Icon getBaseIcon(int side, int meta){
		return blockIcon;
	}
}