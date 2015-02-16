package lordfokas.stargatetech2.api;

import lordfokas.stargatetech2.api.shields.ShieldPermissions;
import lordfokas.stargatetech2.api.stargate.IStargateNetwork;
import lordfokas.stargatetech2.api.stargate.IStargatePlacer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraftforge.fluids.Fluid;

public interface IStargateTechAPI {
	/**
	 * @return The Fluid instance corresponding to Ionized Particles.
	 */
	public Fluid getIonizedParticlesFluidInstance();
	
	/**
	 * @return The creative inventory tab used by StargateTech 2.
	 */
	public CreativeTabs getStargateTab();
	
	/**
	 * @return The IStargateNetwork singleton instance.
	 */
	public IStargateNetwork getStargateNetwork();
	
	/**
	 * @return The IStargatePlacer singleton instance, a.k.a Seeding Ship for the fans.
	 */
	public IStargatePlacer getSeedingShip();
	
	/**
	 * @return The current IFactory instance.
	 */
	public IFactory getFactory();
	
	/**
	 * You do not need to call this method directly as ShieldPermissions will
	 * do it for you. This is here just to remove a core dependency from the
	 * API, which caused an error in the past (import an inaccessible class).
	 * 
	 * @param perms the ShieldPermissions setting.
	 * @param entity The entity to be checked.
	 * @param doDismount If set to true, when an allowed entity is being ridden
	 * by a disallowed one, it's rider will be dismounted so this entity can pass.
	 * @param owner the owner of this shield. Used to check friend permissions.
	 * @return whether or not this entity is allowed through a shield with
	 * these shield permissions.
	 */
	public boolean isEntityAllowed(ShieldPermissions perms, Entity entity, boolean doDismount, String owner);
}