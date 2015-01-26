package lordfokas.stargatetech2.modules.core.machine;

public interface IOwnedMachine {
	public String getOwner();
	public void setOwner(String owner);
	public boolean hasAccess(String player);
}
