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
	private static final Metadata DATA[] = new Metadata[9];
	public static final Metadata PWCR1 =	new Metadata(0, "naquadahPowerCrystal_1",	"Naquadah Power Crystal", 1);
	public static final Metadata PWCR2 =	new Metadata(1, "naquadahPowerCrystal_2",	"Naquadah Power Crystal", 2);
	public static final Metadata PWCR3 =	new Metadata(2, "naquadahPowerCrystal_3",	"Naquadah Power Crystal", 3);
	public static final Metadata INGOT =	new Metadata(3, "naquadahIngot",			"Naquadah Ingot");
	public static final Metadata DUST =		new Metadata(4, "naquadahDust",				"Naquadah Dust");
	public static final Metadata BAR =		new Metadata(5, "naquadahBar",				"Naquadah Bar");
	public static final Metadata PLATE =	new Metadata(6, "naquadahPlate",			"Naquadah Plate");
	public static final Metadata LATTICE =	new Metadata(7, "lattice",					"Semiconductor Lattice Blend");
	public static final Metadata CIRCUIT =	new Metadata(8, "circuitCrystal",			"Circuit Crystal");
	
	private final static String TIERS[] = new String[]{
		"\u00A72Tier I",
		"\u00A76Tier II",
		"\u00A74Tier III"
	};
	
	public static class Metadata{
		public final int ID;
		public final String iconName;
		public final String itemName;
		public final int tier;
		
		public Metadata(int meta, String i, String n){
			this(meta, i, n, 0);
		}
		
		public Metadata(int meta, String i, String n, int t){
			ID = meta;
			DATA[ID] = this;
			iconName = i;
			itemName = n;
			tier = t > 0 ? t-1 : -1;
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
		if(getTierForMeta(meta) != -1){
			list.add(TIERS[DATA[meta].tier]);
		}
	}
	
	public String[] getItemNames(){
		String names[] = new String[DATA.length];
		for(int i = 0; i < DATA.length; i++){
			names[i] = DATA[i].itemName;
		}
		return names;
	}
	
	public static int getTierForMeta(int meta){
		return DATA[meta].tier;
	}
	
	public static int getMetaForTier(int tier){
		for(int i = 0; i < DATA.length; i++){
			Metadata meta = DATA[i];
			if(meta.tier == tier){
				return i;
			}
		}
		return -1;
	}
	
	static{
		for(Metadata data : DATA){
			TextureReference.ITEM_TEXTURES.add(data.iconName);
		}
	}
}