package lordfokas.stargatetech2.enemy.util;

import lordfokas.stargatetech2.enemy.ModuleEnemy;
import lordfokas.stargatetech2.enemy.item.ItemPersonalShield;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EnemyEventHandler {
	@SubscribeEvent
	public void onPlayerDamaged(LivingHurtEvent evt){
		try{
			ItemPersonalShield shield = ModuleEnemy.personalShield;
			EntityLivingBase living = evt.entityLiving;
			if(living instanceof EntityPlayer && shield.blocksDamage(evt.source)){
				EntityPlayer player = (EntityPlayer) living;
				if(player.inventory.hasItem(shield)){
					ItemStack shieldStack = null;
					for(int slot = 0; slot < player.inventory.getSizeInventory(); slot++){
						ItemStack stack = player.inventory.getStackInSlot(slot);
						if(stack != null && stack.getItem() == shield){
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