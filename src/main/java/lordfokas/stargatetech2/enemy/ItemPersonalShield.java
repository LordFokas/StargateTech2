package lordfokas.stargatetech2.enemy;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import lordfokas.stargatetech2.core.base.BaseItem;
import lordfokas.stargatetech2.core.reference.ItemReference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPersonalShield extends BaseItem {
	private static final ArrayList<DamageSource> blockables = new ArrayList<DamageSource>();
	private static final int MAX_DMG = 1000;

	public ItemPersonalShield() {
		super(ItemReference.PERSONAL_SHIELD);
		this.setMaxStackSize(1);
		this.setMaxDamage(MAX_DMG);
		this.setNoRepair();
	}
	
	public boolean blocksDamage(DamageSource source){
		if(source instanceof EntityDamageSource){
			return true;
		}else{
			return blockables.contains(source);
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean ignored){
		int charge = ((MAX_DMG - stack.getItemDamage()) * 100) / MAX_DMG;
		String color = (charge >= 50) ? "a" : (charge >= 25) ? "e" : "c";
		lines.add(String.format("Charge:\u00A7%s %d%%", color, charge));
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack){
		return EnumRarity.epic;
	}
	
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack){
		return true;
	}
	
	static{
		blockables.add(DamageSource.anvil);
		blockables.add(DamageSource.cactus);
		blockables.add(DamageSource.fall);
		blockables.add(DamageSource.fallingBlock);
		blockables.add(DamageSource.generic);
		blockables.add(DamageSource.inFire);
		blockables.add(DamageSource.lava);
		blockables.add(DamageSource.magic);
		blockables.add(DamageSource.onFire);
		blockables.add(DamageSource.wither);
	}
}