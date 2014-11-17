package lordfokas.stargatetech2.core.api;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fluids.Fluid;
import lordfokas.stargatetech2.api.IFactory;
import lordfokas.stargatetech2.api.StargateTechAPI;
import lordfokas.stargatetech2.api.stargate.IStargateNetwork;
import lordfokas.stargatetech2.api.stargate.IStargatePlacer;
import lordfokas.stargatetech2.core.util.StargateLogger;
import lordfokas.stargatetech2.core.util.StargateTab;
import lordfokas.stargatetech2.enemy.util.IonizedParticles;
import lordfokas.stargatetech2.transport.stargates.StargateNetwork;

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
		return StargateTab.instance;
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
}