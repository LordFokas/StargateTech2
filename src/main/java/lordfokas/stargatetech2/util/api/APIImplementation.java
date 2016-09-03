package lordfokas.stargatetech2.util.api;

import lordfokas.stargatetech2.api.IFactory;
import lordfokas.stargatetech2.api.StargateTechAPI;
import lordfokas.stargatetech2.api.shields.ShieldPermissions;
import lordfokas.stargatetech2.api.stargate.IStargateNetwork;
import lordfokas.stargatetech2.api.stargate.IStargatePlacer;
import lordfokas.stargatetech2.modules.enemy.IonizedParticles;
import lordfokas.stargatetech2.modules.integration.te4.CoFHFriendHelper;
import lordfokas.stargatetech2.modules.transport.stargates.StargateNetwork;
import lordfokas.stargatetech2.util.StargateLogger;
import lordfokas.stargatetech2.util.StargateTab;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.Fluid;

public final class APIImplementation extends StargateTechAPI {
	private final IFactory factory = new Factory();
	
	public void enableExternalAccess(){
		StargateLogger.info("Enabling StargateTech2 API.");
		apiInstance = this;
	}
	
	@Override
	public Fluid getIonizedParticlesFluidInstance() {
		return IonizedParticles.fluid;
	}
	
	@Override
	public CreativeTabs getStargateTab() {
		return StargateTab.INSTANCE;
	}
	
	@Override
	public IStargateNetwork getStargateNetwork() {
		return StargateNetwork.instance();
	}
	
	@Override
	public IStargatePlacer getSeedingShip() {
		return SeedingShip.SHIP;
	}
	
	@Override
	public IFactory getFactory() {
		return factory;
	}

	@Override
	public boolean isEntityAllowed(ShieldPermissions perms, Entity entity, boolean doDismount, String owner) {
		boolean allow = false;
		if(entity instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer) entity;
			if(CoFHFriendHelper.isSystemEnabled()){
				if(CoFHFriendHelper.isFriend(player.getName(), owner)){ // XXX getName() might not be suitable
					allow = perms.hasBit(ShieldPermissions.PERM_FRIEND);
				}else{
					allow = perms.hasBit(ShieldPermissions.PERM_PLAYER);
				}
			}else{
				allow = perms.hasBit(ShieldPermissions.PERM_PLAYER);
			}
			if(perms.getExceptionList().contains(player.getDisplayName())){
				allow = !allow;
			}
		}else if(entity instanceof EntityVillager){
			allow = perms.hasBit(ShieldPermissions.PERM_VILLAGER);
		}else if(entity instanceof EntityAnimal){
			allow = perms.hasBit(ShieldPermissions.PERM_ANIMAL);
		}else if(entity instanceof EntityMob){
			allow = perms.hasBit(ShieldPermissions.PERM_MONSTER);
		}else if(entity instanceof EntityMinecart){
			allow = perms.hasBit(ShieldPermissions.PERM_VESSEL);
		}
		
		// Dismount riders that are not allowed through.
		if(doDismount && allow && !entity.worldObj.isRemote && entity.isBeingRidden()){
			for(Entity rider : entity.getPassengers()){
				if(!isEntityAllowed(perms, rider, true, owner)){
					rider.dismountRidingEntity();
				}
			}
		}
		
		return allow;
	}
}