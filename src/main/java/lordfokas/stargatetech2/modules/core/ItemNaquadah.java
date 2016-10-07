package lordfokas.stargatetech2.modules.core;

import java.util.List;

import lordfokas.naquadria.item.BaseItem;
import lordfokas.stargatetech2.reference.ItemReference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;

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
		ModelLoader.setCustomModelResourceLocation(this, Type.INGOT.ordinal(), new ModelResourceLocation("stargatetech2:naquadah_ingot"));
		ModelLoader.setCustomModelResourceLocation(this, Type.DUST.ordinal(), new ModelResourceLocation("stargatetech2:naquadah_dust"));
		ModelLoader.setCustomModelResourceLocation(this, Type.PLATE.ordinal(), new ModelResourceLocation("stargatetech2:naquadah_plate"));
		ModelLoader.setCustomModelResourceLocation(this, Type.CIRCUIT.ordinal(), new ModelResourceLocation("stargatetech2:naquadah_circuit"));
		ModelLoader.setCustomModelResourceLocation(this, Type.COIL_NAQ.ordinal(), new ModelResourceLocation("stargatetech2:naquadah_coil"));
		ModelLoader.setCustomModelResourceLocation(this, Type.COIL_END.ordinal(), new ModelResourceLocation("stargatetech2:naquadah_coil_end"));
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