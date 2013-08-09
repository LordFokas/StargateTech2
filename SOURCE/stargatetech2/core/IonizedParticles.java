package stargatetech2.core;

import net.minecraftforge.fluids.Fluid;

public class IonizedParticles extends Fluid{
	public static final IonizedParticles fluid = new IonizedParticles();
	
	private IonizedParticles() {
		super("Ionized Particles");
		setUnlocalizedName("ionizedParticles");
		setDensity(0);
		setGaseous(true);
		setLuminosity(15);
		setViscosity(0);
		setTemperature(800); // 800ºK ~= 525ºC
	}
}