package lordfokas.stargatetech2.util.api;

import lordfokas.stargatetech2.api.IFactory;
import lordfokas.stargatetech2.api.StargateTechAPI;
import lordfokas.stargatetech2.api.stargate.IStargateNetwork;
import lordfokas.stargatetech2.api.stargate.IStargatePlacer;
import lordfokas.stargatetech2.modules.enemy.IonizedParticles;
import lordfokas.stargatetech2.modules.transport.stargates.StargateNetwork;
import lordfokas.stargatetech2.util.StargateLogger;
import lordfokas.stargatetech2.util.StargateTab;
import net.minecraft.creativetab.CreativeTabs;
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