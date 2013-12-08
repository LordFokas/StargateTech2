package stargatetech2.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fluids.Fluid;
import stargatetech2.StargateTech2;
import stargatetech2.api.StargateTechAPI;
import stargatetech2.api.stargate.IStargateNetwork;
import stargatetech2.common.util.StargateLogger;
import stargatetech2.common.util.StargateTab;
import stargatetech2.core.network.stargate.StargateNetwork;
import stargatetech2.core.util.IonizedParticles;

public final class APIImplementation extends StargateTechAPI {
	private static final int VERSION_MAJOR	= 0;
	private static final int VERSION_MINOR	= 3;
	private static final int VERSION_MNT	= 0;
	private static final int VERSION_REV	= 8;
	private static final String API_STATUS	= "dev";
	
	public void enableExternalAccess(){
		StargateLogger.info("Enabling StargateTech2 API.");
		StargateLogger.info("API Version: " + getVersionString());
		apiInstance = this;
	}
	
	@Override
	public String getVersionString() {
		return VERSION_MAJOR + "." + VERSION_MINOR + "." + VERSION_MNT + "." + VERSION_REV + "-" + API_STATUS;
	}

	@Override
	public int[] getVersionNumbers() {
		return new int[]{VERSION_MAJOR, VERSION_MINOR, VERSION_MNT, VERSION_REV};
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
}