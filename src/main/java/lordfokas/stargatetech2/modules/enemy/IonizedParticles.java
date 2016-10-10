package lordfokas.stargatetech2.modules.enemy;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class IonizedParticles extends Fluid{
	private static final ResourceLocation TEXTURE = new ResourceLocation("stargatetech2", "textures/blocks/ionized_particles");
	public static final IonizedParticles fluid = new IonizedParticles();
	
	private IonizedParticles() {
		super("Ionized Particles", TEXTURE, TEXTURE);
		setUnlocalizedName("stargatetech2.ionizedParticles.name");
		setDensity(0);
		setGaseous(true);
		setLuminosity(15);
		setTemperature(520);
	}
}