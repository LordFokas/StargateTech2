package stargatetech2.core.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import stargatetech2.core.base.BaseItem;
import stargatetech2.core.reference.ItemReference;
import stargatetech2.core.reference.TextureReference;
import stargatetech2.core.util.IconRegistry;

public class ItemNaquadah extends BaseItem{
	public static final Metadata DATA[] = new Metadata[7];
	public static final Metadata INGOT =	new Metadata(0, "naquadahIngot",	"Naquadah Ingot");
	public static final Metadata DUST =		new Metadata(1, "naquadahDust",		"Naquadah Dust");
	public static final Metadata PLATE =	new Metadata(2, "naquadahPlate",	"Naquadah Plate");
	public static final Metadata LATTICE =	new Metadata(3, "lattice",			"Semiconductor Lattice Blend");
	public static final Metadata CIRCUIT =	new Metadata(4, "circuitCrystal",	"Circuit Crystal");
	public static final Metadata COIL_NAQ =	new Metadata(5, "coilNaquadah",		"Magnetic Coil");
	public static final Metadata COIL_END =	new Metadata(6, "coilEnder",		"Matter Coil");
	
	public static class Metadata{
		public final int ID;
		public final String iconName;
		public final String itemName;
		
		public Metadata(int meta, String i, String n){
			ID = meta;
			DATA[ID] = this;
			iconName = i;
			itemName = n;
		}
	}
	
	public ItemNaquadah() {
		super(ItemReference.NAQUADAH);
		setHasSubtypes(true);
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack){
		return DATA[stack.getItemDamage()].itemName;
	}
	
	@Override
	public String getItemDisplayName(ItemStack stack){
		return DATA[stack.getItemDamage()].itemName;
	}
	
	@Override
	public Icon getIconFromDamage(int meta){
		return IconRegistry.itemIcons.get(DATA[meta].iconName);
	}
	
	@Override
	public void getSubItems(int id, CreativeTabs tab, List list){
		for(int i = 0; i < DATA.length; i++){
			list.add(new ItemStack(id, 1, i));
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean ignored){
		int meta = stack.getItemDamage();
	}
	
	public String[] getItemNames(){
		String names[] = new String[DATA.length];
		for(int i = 0; i < DATA.length; i++){
			names[i] = DATA[i].itemName;
		}
		return names;
	}
	
	static{
		for(Metadata data : DATA){
			TextureReference.ITEM_TEXTURES.add(data.iconName);
		}
	}
}