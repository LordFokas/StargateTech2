package stargatetech2.core.util;

import net.minecraftforge.fluids.Fluid;

public class IonizedParticles extends Fluid{
	public static final IonizedParticles fluid = new IonizedParticles();
	
	private IonizedParticles() {
		super("Ionized Particles");
		setUnlocalizedName("ionizedParticles");
		setDensity(0);
		setGaseous(true);
		setLuminosity(15);
		// setViscosity(1000); // Unable to determine a good value.
		setTemperature(800); // 800ºK ~= 525ºC
	}
}