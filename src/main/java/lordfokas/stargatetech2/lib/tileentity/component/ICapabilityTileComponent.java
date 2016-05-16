package lordfokas.stargatetech2.lib.tileentity.component;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public interface ICapabilityTileComponent<C> extends ITileComponent{
	/** The value returned here is cached in the TE for long periods of time.
	 * This should return a delegator implementation and not an ephemeral object
	 */
	public C getCapability(EnumFacing side);
	
	/** The unique identifier for the capability */
	public Capability<C> getCapability();
	
	/** Defines if the component should be destroyed in case it returns <code>null</code>
	 * from {@link getCapability()} */
	public boolean discardIfNull();
}
