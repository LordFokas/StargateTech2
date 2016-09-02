package lordfokas.stargatetech2.modules.core;

import java.util.List;

import lordfokas.naquadria.item.BaseItem;
import lordfokas.stargatetech2.reference.ItemReference;
import lordfokas.stargatetech2.reference.TextureReference;
import lordfokas.stargatetech2.util.IconRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemNaquadah extends BaseItem{
	public static enum Type{
		INGOT("naquadahIngot", "Naquadah Ingot"),
		DUST("naquadahDust", "Naquadah Dust"),
		PLATE("naquadahPlate", "Naquadah Plate"),
		CIRCUIT("circuitCrystal", "Circuit Crystal"),
		COIL_NAQ("coilNaquadah", "Magnetic Induction Coil"),
		COIL_END("coilEnder", "Matter Conductance Coil");
		
		public final String name;
		public final String itemName;
		
		private Type(String i, String n){
			name = i;
			itemName = n;
		}
	}
	
	public ItemNaquadah() {
		super(ItemReference.NAQUADAH);
		setHasSubtypes(true);
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list){
		for(Type type : Type.values()){
			list.add(new ItemStack(this, 1, type.ordinal()));
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack){
		return "item." + Type.values()[stack.getItemDamage()].name;
	}
}