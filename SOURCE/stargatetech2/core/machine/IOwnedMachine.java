package stargatetech2.core.machine;

public interface IOwnedMachine {
	public String getOwner();
	public void setOwner(String owner);
	public boolean hasAccess(String player);
}
