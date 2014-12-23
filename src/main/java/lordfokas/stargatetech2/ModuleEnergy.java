package lordfokas.stargatetech2;


public class ModuleEnergy implements IContentModule {

	@Override
	public void preInit(){
		
	}

	@Override
	public void init(){
		
	}

	@Override
	public void postInit(){
		StargateTech2.proxy.registerRenderers(Module.ENERGY);
	}

	@Override public void onServerStart(){}
	@Override public void onServerStop(){}

	@Override
	public String getModuleName(){
		return "Energy";
	}
}