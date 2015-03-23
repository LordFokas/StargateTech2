package lordfokas.stargatetech2.lib.tileentity;

public interface IRedstoneAware {
	public void setUsesRedstone(boolean redstone);
	public void onRedstoneState(boolean powered);
}
