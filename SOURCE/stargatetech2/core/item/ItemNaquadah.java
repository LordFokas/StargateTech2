package stargatetech2.core.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import stargatetech2.common.base.BaseItem;
import stargatetech2.common.reference.ItemReference;
import stargatetech2.common.reference.TextureReference;
import stargatetech2.common.util.IconRegistry;
import stargatetech2.common.util.StargateTab;

public class ItemNaquadah extends BaseItem{
	private static final Metadata[] DATA = new Metadata[]{
		new Metadata("naquadahIngot",	"Naquadah Ingot",	0),
		new Metadata("naquadahDust",	"Naquadah Dust",	0),
		new Metadata("naquadahBar",		"Naquadah Bar",		0),
		new Metadata("naquadahPlate",	"Naquadah Plate",	0),
		new Metadata("naquadahPowerCrystal_1", "Naquadah Power Crystal", 1),
		new Metadata("naquadahPowerCrystal_2", "Naquadah Power Crystal", 2),
		new Metadata("naquadahPowerCrystal_3", "Naquadah Power Crystal", 3),
		new Metadata("circuitCrystal",	"Circuit Crystal",	0),
		new Metadata("lattice",	"Semiconductor Lattice",	0),
	};
	
	private final static String TIERS[] = new String[]{
		"\u00A72Tier I",
		"\u00A76Tier II",
		"\u00A74Tier III"
	};
	
	private static class Metadata{
		public final String iconName;
		public final String itemName;
		public final int tier;
		
		public Metadata(String i, String n, int t){
			iconName = i;
			itemName = n;
			tier = t > 0 ? t-1 : -1;
		}
	}
	
	public ItemNaquadah() {
		super(ItemReference.NAQUADAH);
		setHasSubtypes(true);
		StargateTab.iconID = itemID;
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