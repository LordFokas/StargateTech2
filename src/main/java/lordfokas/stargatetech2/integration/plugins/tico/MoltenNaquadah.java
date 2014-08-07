package lordfokas.stargatetech2.integration.plugins.tico;

import net.minecraftforge.fluids.Fluid;

public class MoltenNaquadah extends Fluid {
	public static final MoltenNaquadah instance = new MoltenNaquadah();
	
	public MoltenNaquadah(){
		super("molten_naquadah");
		setDensity(120000); // Naquadah is heavy as fuck, what did you expect?
		setLuminosity(15);
		setTemperature(800); // 800K ~= 525C
	}
	
	@Override
	public String getUnlocalizedName(){
		return "StargateTech2:fluid.moltenNaquadah";
	}
}
