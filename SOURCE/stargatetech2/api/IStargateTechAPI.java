package stargatetech2.api;

import stargatetech2.api.stargate.IStargateNetwork;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fluids.Fluid;

public interface IStargateTechAPI {
	
	/**
	 * @return A version string in the following format:<br>
	 * {Major}.{Minor}.{Maintenance}.{RevisionNumber}-{Status}
	 */
	public String getVersionString();
	
	/**
	 * Array Indexes:<br>
	 * 0 - Major<br>
	 * 1 - Minor<br>
	 * 2 - Maintenance (Incremented on Bug fixes)<br>
	 * 3 - Revision Number (Incremented every time there is a change)
	 * 
	 * @return An array with the 4 version numbers.
	 */
	public int[] getVersionNumbers();
	
	/**
	 * @return The Fluid instance corresponding to Ionized Particles.
	 */
	public Fluid getIonizedParticlesFluidInstance();
	
	/**
	 * @return The creative inventory tab used by StargateTech 2.
	 */
	public CreativeTabs getStargateTab();
	
	/**
	 * @return the IStargateNetwork singleton instance.
	 */
	public IStargateNetwork getStargateNetwork();
}