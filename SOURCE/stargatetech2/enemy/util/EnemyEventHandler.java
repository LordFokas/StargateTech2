package stargatetech2.enemy.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import stargatetech2.enemy.ModuleEnemy;
import stargatetech2.enemy.item.ItemPersonalShield;

public class EnemyEventHandler {
	@ForgeSubscribe
	public void onPlayerDamaged(LivingHurtEvent evt){
		try{
			ItemPersonalShield shield = ModuleEnemy.personalShield;
			EntityLivingBase living = evt.entityLiving;
			if(living instanceof EntityPlayer && shield.blocksDamage(evt.source)){
				EntityPlayer player = (EntityPlayer) living;
				if(player.inventory.hasItem(shield.itemID)){
					ItemStack shieldStack = null;
					for(int slot = 0; slot < player.inventory.getSizeInventory(); slot++){
						ItemStack stack = player.inventory.getStackInSlot(slot);
						if(stack != null && stack.itemID == shield.itemID){
							stack.damageItem(1, player);
							evt.setCanceled(true);
							if(stack.stackSize == 0){
								player.inventory.setInventorySlotContents(slot, null);
							}
							break;
						}
					}
				}
			}
		}catch(Exception ignored){}
	}
}