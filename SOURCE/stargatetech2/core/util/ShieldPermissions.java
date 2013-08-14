package stargatetech2.core.util;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

// There's room here for a full-blown system attached to the API.
// Other modders could add entities and filters / permissions for those.

public class ShieldPermissions {
	public static final int PERM_PLAYER		= 0x01;
	public static final int PERM_VILLAGER	= 0x02;
	
	public static final int PERM_ANIMAL		= 0x04;
	public static final int PERM_MONSTER	= 0x08;
	
	public static final int PERM_MINECART	= 0x10;
	
	private int permValue = 0;
	private ArrayList<String> playerExceptions = new ArrayList<String>();
	
	public static ShieldPermissions getDefault(){
		ShieldPermissions perm = new ShieldPermissions();
		perm.allow(PERM_PLAYER);
		return perm;
	}
	
	public void allow(int perm){
		permValue |= perm;
	}
	
	public void disallow(int perm){
		permValue &= ~perm;
	}
	
	public void setPlayerException(String player){
		if(!playerExceptions.contains(player))
			playerExceptions.add(player);
	}
	
	public void removePlayerException(String player){
		playerExceptions.remove(player);
	}
	
	public ArrayList<String> getExceptionList(){
		return playerExceptions;
	}
	
	public boolean isEntityAllowed(Entity entity){
		// PERM_PLAYER
		if(entity instanceof EntityPlayer){
			boolean allow = false;
			if(playerExceptions.contains(entity.getEntityName())){
				allow = true;
			}
			if(hasBit(PERM_PLAYER)){
				allow = !allow;
			}
			return allow;
		}
		
		// PERM_VILLAGER
		if(entity instanceof EntityVillager){
			return hasBit(PERM_VILLAGER);
		}
		
		// PERM_ANIMAL
		if(entity instanceof EntityAnimal){
			return hasBit(PERM_ANIMAL);
		}
		
		// PERM_MONSTER
		if(entity instanceof EntityMob){
			return hasBit(PERM_MONSTER);
		}
		
		// PERM_MINECART
		if(entity instanceof EntityMinecart){
			return hasBit(PERM_MINECART);
		}
		
		return false;
	}
	
	public boolean hasBit(int bit){
		return (permValue & bit) != 0;
	}
	
	public static ShieldPermissions readFromNBT(NBTTagCompound nbt){
		ShieldPermissions permissions = getDefault();
		if(nbt != null){
			int exceptions = nbt.getInteger("exceptions");
			permissions.permValue = nbt.getInteger("permValue");
			permissions.playerExceptions = new ArrayList<String>(exceptions);
			for(int i = 0; i < exceptions; i++){
				permissions.setPlayerException(nbt.getString("pex" + i));
			}
		}
		return permissions;
	}
	
	public NBTTagCompound writeToNBT(){
		NBTTagCompound nbt = new NBTTagCompound();
		int exceptions = playerExceptions.size();
		nbt.setInteger("permValue", permValue);
		nbt.setInteger("exceptions", exceptions);
		for(int i = 0; i < exceptions; i++){
			nbt.setString("pex" + i, playerExceptions.get(i));
		}
		return nbt;
	}
}