package lordfokas.stargatetech2.modules.enemy;

import net.minecraftforge.fluids.Fluid;

public class IonizedParticles extends Fluid{
	public static final IonizedParticles fluid = new IonizedParticles();
	
	private IonizedParticles() {
		super("Ionized Particles");
		setUnlocalizedName("ionizedParticles");
		setDensity(0);
		setGaseous(true);
		setLuminosity(15);
		setTemperature(800); // 800K ~= 525C
	}
}