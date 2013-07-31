package stargatetech2.common.util;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class MaterialNaquadah extends Material {
	
	public static final MaterialNaquadah unbreakable = new MaterialNaquadah(true);
	public static final MaterialNaquadah breakable = new MaterialNaquadah(false);
	
	protected MaterialNaquadah(boolean requiresTool){
		super(MapColor.ironColor);
		this.setImmovableMobility();
		if(requiresTool){
			this.setRequiresTool();
		}
	}
}