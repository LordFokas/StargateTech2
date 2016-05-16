package lordfokas.naquadria.tileentity;

public interface IRedstoneAware {
	public void setUsesRedstone(boolean redstone);
	public void onRedstoneState(boolean powered);
}
