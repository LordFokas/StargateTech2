package stargatetech2.core.util;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fluids.Fluid;
import stargatetech2.api.IFactory;
import stargatetech2.api.StargateTechAPI;
import stargatetech2.api.stargate.IStargateNetwork;
import stargatetech2.enemy.util.IonizedParticles;
import stargatetech2.transport.stargates.StargateNetwork;

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
	public IFactory getFactory() {
		return factory;
	}
}