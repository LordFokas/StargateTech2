package lordfokas.stargatetech2.modules.core;

import java.util.List;

import lordfokas.stargatetech2.lib.item.BaseItem;
import lordfokas.stargatetech2.lib.util.NoDefaultTexture;
import lordfokas.stargatetech2.reference.ItemReference;
import lordfokas.stargatetech2.reference.TextureReference;
import lordfokas.stargatetech2.util.IconRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

@NoDefaultTexture
public class ItemNaquadah extends BaseItem{
	public static final Metadata DATA[] = new Metadata[6];
	public static final Metadata INGOT =	new Metadata(0, "naquadahIngot",	"Naquadah Ingot");
	public static final Metadata DUST =		new Metadata(1, "naquadahDust",		"Naquadah Dust");
	public static final Metadata PLATE =	new Metadata(2, "naquadahPlate",	"Naquadah Plate");
	public static final Metadata CIRCUIT =	new Metadata(3, "circuitCrystal",	"Circuit Crystal");
	public static final Metadata COIL_NAQ =	new Metadata(4, "coilNaquadah",		"Magnetic Induction Coil");
	public static final Metadata COIL_END =	new Metadata(5, "coilEnder",		"Matter Conductance Coil");
	
	public static class Metadata{
		public final int ID;
		public final String name;
		public final String itemName;
		
		public Metadata(int meta, String i, String n){
			ID = meta;
			DATA[ID] = this;
			name = i;
			itemName = n;
		}
	}
	
	public ItemNaquadah() {
		super(ItemReference.NAQUADAH);
		setHasSubtypes(true);
	}
	
	@Override
	public IIcon getIconFromDamage(int meta){
		return IconRegistry.itemIcons.get(DATA[meta].name);
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list){
		for(int i = 0; i < DATA.length; i++){
			list.add(new ItemStack(this, 1, i));
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean ignored){
		// int meta = stack.getItemDamage();
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack){
		return "item." + DATA[stack.getItemDamage()].name;
	}
	
	static{
		for(Metadata data : DATA){
			TextureReference.ITEM_TEXTURES.add(data.name);
		}
	}
}