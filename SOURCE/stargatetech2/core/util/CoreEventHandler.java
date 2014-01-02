package stargatetech2.core.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import stargatetech2.api.bus.BusEvent;
import stargatetech2.common.util.Vec3Int;
import stargatetech2.core.ModuleCore;
import stargatetech2.core.item.ItemPersonalShield;
import stargatetech2.core.network.bus.RecursiveBusRemapper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CoreEventHandler {
	@ForgeSubscribe
	@SideOnly(Side.CLIENT)
	public void handleBlockHighlight(DrawBlockHighlightEvent evt){
		MovingObjectPosition mop = evt.target;
		World world = evt.context.theWorld;
		int blockID = world.getBlockId(mop.blockX, mop.blockY, mop.blockZ);
		if(blockID == ModuleCore.shield.blockID || blockID == ModuleCore.invisible.blockID){
			evt.setCanceled(true);
		}
	}
	
	@ForgeSubscribe
	public void onPlayerDamaged(LivingHurtEvent evt){
		try{
			ItemPersonalShield shield = ModuleCore.personalShield;
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
	
	@ForgeSubscribe
	public void remapAbstractBus(BusEvent evt){
		if(!evt.world.isRemote){
			if(evt instanceof BusEvent.RemoveFromNetwork){
				Vec3Int position = new Vec3Int(evt.x, evt.y, evt.z);
				for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
					RecursiveBusRemapper.scan(evt.world, position.offset(dir));
				}
			}else if(evt instanceof BusEvent.AddToNetwork){
				RecursiveBusRemapper.scan(evt.world, new Vec3Int(evt.x, evt.y, evt.z));
			}
		}
	}
}