package lordfokas.stargatetech2.integration.plugins;

public interface IPlugin {
	public void load();
	public void postload();
	public void fallback();
}
