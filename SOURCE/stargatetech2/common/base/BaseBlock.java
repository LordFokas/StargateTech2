package stargatetech2.common.base;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import stargatetech2.StargateTech2;
import stargatetech2.common.reference.ModReference;
import stargatetech2.common.util.MaterialNaquadah;

public class BaseBlock extends Block{
	
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
		this.setCreativeTab(CreativeTabs.tabBlock);
	}
	
	@Override
	public int getMobilityFlag(){
		return 2;
	}
	
	public final void registerBlock(){
		GameRegistry.registerBlock(this, getUnlocalizedName());
	}
}