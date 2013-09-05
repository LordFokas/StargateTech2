package stargatetech2;

public interface IContentModule {
	public enum Module{
		CORE,
		INTEGRATION
	}
	
	public void preInit();
	public void init();
	public void postInit();
	public void onServerStart();
	public void onServerStop();
	public String getModuleName();
}
